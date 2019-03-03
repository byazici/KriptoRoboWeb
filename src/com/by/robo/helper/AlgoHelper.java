package com.by.robo.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.dao.AlgoDao;
import com.by.robo.dao.OrderDao;
import com.by.robo.dao.TickDao;
import com.by.robo.dao.UserDao;
import com.by.robo.enums.AlgoStatus;
import com.by.robo.enums.BuySell;
import com.by.robo.enums.OrderStatus;
import com.by.robo.enums.PairSymbol;
import com.by.robo.enums.TrueFalse;
import com.by.robo.model.Algo;
import com.by.robo.model.Order;
import com.by.robo.model.OrderResult;
import com.by.robo.model.Result;
import com.by.robo.model.Tick;
import com.by.robo.model.User;
import com.by.robo.model.UserAlgoLimits;
import com.by.robo.server.AlgoServer;
import com.by.robo.utils.DateUtils;
import com.by.robo.utils.MailUtils;
import com.by.robo.utils.NumberUtils;

public class AlgoHelper {
	final static Logger logger = LoggerFactory.getLogger(AlgoHelper.class);

	public void executeAllAlgo(Tick t) {
		List<Algo> algoList = null;
		Long l1 = new Date().getTime();

		algoList = AlgoDao.getActiveAlgoList(t);

		try {
			for (Algo algo : algoList) {
				executeAlgo(algo, t);
			}

			Long l2 = new Date().getTime();
			logger.info("# " + (algoList != null ? algoList.size() : 0) + " algo executed in " + (l2 - l1) + " ms.");

		} catch (Exception e) {
			logger.error("error here", e);
		}
	}

	public void executeAlgo(Algo algo, Tick tick) {

		switch (algo.getStatus()) {
		case NEW:
			executeNew(algo);
			break;

		case BUY_OPEN:
			executeBuyOpen(algo, tick);
			break;

		case TRIG_WAIT:
			executeTrigWait(algo, tick);
			break;

		case SELL_WAIT:
			executeSellWait(algo, tick);
			break;

		case SELL_OPEN:
			executeSellOpen(algo, tick);
			break;

		default:
			executeDefault(algo);
			break;
		}
	}

	private void executeSellWait(Algo algo, Tick tick) {
		OrderHelper oHelper = new OrderHelper();
		Order sell = oHelper.getSellOrderByAlgo(algo);
		Order buy = oHelper.getBuyAlgoOrder(algo);
		Date now = AlgoServer.getAlgoDate();

		if (buy == null) {
			algo.setStatus(AlgoStatus.ERROR);
			algo.setDescr("Buy order is empty!");
			updateAlgoStatus(algo);
			logger.error(algoLogger(algo));

		} else if (sell != null) {
			algo.setStatus(AlgoStatus.ERROR);
			algo.setDescr("Sell order must empty!");
			updateAlgoStatus(algo);
			logger.error(algoLogger(algo));

		} else {
			if (algo.getTrigPrice().compareTo(tick.getLast()) <= 0) { // fiyat tırmanmış
				BigDecimal diff = tick.getLast().subtract(algo.getTrigPrice());

				algo.setTrigPrice(algo.getTrigPrice().add(diff)); // trigger yukarı
				algo.setSellPrice(algo.getSellPrice().add(diff)); // satış fiyatı da yukarı

				algo.setDescr("TRIG-UP (" + tick.getLast() + ")");
				updateAlgo(algo);
				// MailUtils.sendEmailToUser(algo.getUserId(), "Algo Status Change: TRIG-UP",
				// "#" + algo.getId() + " New Trigger: " + tick.getLast());
				logger.info(algoLogger(algo));

			} else {
				if (algo.getSellPrice().compareTo(tick.getLast()) >= 0) {
					Order o = new Order(algo.getPairSynbol(), BuySell.SELL, now, buy.getPrice(), buy.getAmount(),
							TrueFalse.FALSE, OrderStatus.OPEN, algo.getId(), "CREATED.", algo.getUserId());

					OrderResult or = oHelper.createOrder(o);
					if (or == null) {
						// iletişim vb hatası alındı, bir daha dene
						algo.setDescr("Generic Sell error");
						updateAlgoStatus(algo);
						logger.error(algoLogger(algo));
					} else if (!or.isSuccess()) {
						// mantıksal hata alındı, hataya çek
						// algo.setStatus(AlgoStatus.ERROR); tekrar dene
						algo.setDescr("Sell error: " + or.getErrMsg());
						updateAlgoStatus(algo);
						logger.error(algoLogger(algo));
					} else {
						algo.setStatus(AlgoStatus.SELL_OPEN);
						algo.setDescr("to SELL-OPEN (" + tick.getLast() + ")");
						algo.setSellId(or.getDbId());
						updateAlgoSellId(algo);
						logger.info(algoLogger(algo));
					}
				} else {
					// next time
				}
			}
		}
	}

	private void executeTrigWait(Algo algo, Tick tick) {
		OrderHelper oHelper = new OrderHelper();
		Order sell = oHelper.getSellOrderByAlgo(algo);

		if (sell != null) {
			algo.setStatus(AlgoStatus.ERROR);
			algo.setDescr("Sell order must empty!");
			updateAlgoStatus(algo);
			logger.error(algoLogger(algo));
		} else {
			if (algo.getTrigPrice().compareTo(tick.getLast()) <= 0) {
				algo.setStatus(AlgoStatus.SELL_WAIT);
				algo.setDescr("to SELL-WAIT (" + tick.getLast() + ")");
				updateAlgoStatus(algo);
				MailUtils.sendEmailToUser(algo.getUserId(), "Algo Durumu #" + algo.getId(),
						"Algo tetiği aşıldı. Satış bekleniyor." + getAlgoInfoForMail(algo));
				logger.info(algoLogger(algo));
			} else {
				// next time
			}
		}
	}

	private void executeBuyOpen(Algo algo, Tick tick) {
		OrderHelper oHelper = new OrderHelper();
		Order buy = oHelper.getBuyAlgoOrder(algo);
		Date xDate = AlgoServer.getAlgoDate();
		User user = UserDao.getUser(algo.getUserId());

		if (user == null) {
			algo.setStatus(AlgoStatus.ERROR);
			algo.setDescr("userId is empty!");
			updateAlgoStatus(algo);
			logger.error(algoLogger(algo));
		} else if (buy == null) {
			algo.setStatus(AlgoStatus.ERROR);
			algo.setDescr("Buy order is empty!");
			updateAlgoStatus(algo);
			logger.error(algoLogger(algo));
		} else {
			switch (buy.getStatus()) {
			case OPEN:
				if (algo.getExpireDate().compareTo(xDate) < 0) { // expired
					if (oHelper.cancelOrder(buy)) {
						// if (algo.getRepeated().equals(TrueFalse.TRUE)) {
						// algo.setStatus(AlgoStatus.CANCEL);
						// algo.setDescr("CANCEL_WITH_REPEAT");
						// updateAlgoStatus(algo);
						// repeatAlgo(algo);
						// } else {
						// algo.setStatus(AlgoStatus.CANCEL);
						// algo.setDescr("EXPIRED");
						// updateAlgoStatus(algo);
						// logger.info(algoLogger(2, algo));
						// }
						algo.setStatus(AlgoStatus.CANCEL);
						algo.setDescr("CANCEL_WITH_REPEAT");
						updateAlgoStatus(algo);
						repeatAlgo(user, algo);
						logger.info(algoLogger(algo));

					} else {
						// iptal denemesinde hata alırsak, 1dk sonra bir daha dene.
						// algo.setStatus(AlgoStatus.ERROR);
						algo.setDescr("Expire order cancel error!");
						updateAlgoStatus(algo);
						logger.error(algoLogger(algo));
					}
				} else {
					// next time
				}

				break;

			case RLZ:
				algo.setStatus(AlgoStatus.TRIG_WAIT);
				algo.setDescr("to TRIG-WAIT (" + tick.getLast() + ")");
				updateAlgoStatus(algo);
				MailUtils.sendEmailToUser(algo.getUserId(), 
						"Algo Durumu #" + algo.getId(),
						"Alış gerçekleşti." + algo.getPairSynbol() + " " + buy.getAmount() + "x" + buy.getPrice() + getAlgoInfoForMail(algo));
				logger.info(algoLogger(algo));
				break;

			default:
				// iptal ile çakışıyor bazen.
				algo.setStatus(AlgoStatus.ERROR);
				algo.setDescr("Invalid buy status! (" + buy.getStatus() + ")");
				// updateAlgoStatus(algo);
				logger.error(algoLogger(algo));
				break;
			}
		}
	}
	
	private String getAlgoInfoForMail(Algo algo) {
		StringBuffer s = new StringBuffer();
		s.append("<p /><b>Algo Detayları</b>");
		s.append("<p /><hr />");
		s.append("Adı : ").append(algo.getName()).append(" <br />");
		s.append("Tarih : ").append(algo.getCreateDate()).append(" <br />");
		s.append("Alış : ").append(NumberUtils.formatDec6(algo.getBuyPrice())).append(" TL<br />");
		s.append("Tetik : ").append(NumberUtils.formatDec6(algo.getTrigPrice())).append(" TL<br />");
		s.append("Satış : ").append(NumberUtils.formatDec6(algo.getSellPrice())).append(" TL<br />");
		s.append("Ort.Fiyat : ").append(NumberUtils.formatDec6(algo.getAvgPrice())).append(" TL<br />");
		s.append("Baz Fiyat : ").append(NumberUtils.formatDec6(algo.getBasePrice())).append(" TL<br />");
		s.append("Süre : ").append(algo.getDuration()).append(" saat <br />");
		s.append("Açıklama : ").append(algo.getDescr()).append(" <br />");
		s.append("<p /><hr />");
		
		return s.toString();	
	}

	private void executeNew(Algo algo) {
		OrderHelper oHelper = new OrderHelper();
		Order buy = oHelper.getBuyAlgoOrder(algo);
		Date now = AlgoServer.getAlgoDate();

		if (buy != null) {
			algo.setStatus(AlgoStatus.ERROR);
			algo.setDescr("Buy order must empty!");
			updateAlgoStatus(algo);
			logger.error(algoLogger(algo));
		} else {
			Order o = new Order(algo.getPairSynbol(), BuySell.BUY, now, algo.getBuyPrice(),
					algo.getMaxAmount().divide(algo.getBuyPrice(), 6, RoundingMode.FLOOR), TrueFalse.FALSE,
					OrderStatus.OPEN, algo.getId(), "CREATED", algo.getUserId());

			OrderResult or = oHelper.createOrder(o);
			if (or == null) {
				// iletişim vb hatası alındı, bir daha dene
				algo.setDescr("Generic Buy error!");
				updateAlgoStatus(algo);
				logger.error(algoLogger(algo));
			} else if (!or.isSuccess()) {
				// mantıksal hata alındı, hataya çek
				algo.setStatus(AlgoStatus.ERROR);
				algo.setDescr("Buy error: " + or.getErrMsg());
				updateAlgoStatus(algo);
				logger.error(algoLogger(algo));
			} else {
				algo.setStatus(AlgoStatus.BUY_OPEN);
				algo.setDescr("to BUY_OPEN");
				algo.setBuyId(or.getDbId());
				updateAlgoBuyId(algo);
				logger.info(algoLogger(algo));
			}
		}
	}

	private void executeSellOpen(Algo algo, Tick tick) {
		OrderHelper oHelper = new OrderHelper();
		Order sell = oHelper.getSellOrderByAlgo(algo);
		Order buy = oHelper.getBuyAlgoOrder(algo);
		User user = UserDao.getUser(algo.getUserId());

		if (user == null) {
			algo.setStatus(AlgoStatus.ERROR);
			algo.setDescr("userId is empty!");
			updateAlgoStatus(algo);
			logger.error(algoLogger(algo));
		} else if (sell == null) {
			algo.setStatus(AlgoStatus.ERROR);
			algo.setDescr("Sell order is empty!");
			updateAlgoStatus(algo);
			logger.error(algoLogger(algo));
		} else {
			switch (sell.getStatus()) {
			case OPEN:
				// next time
				break;
			case RLZ:
				BigDecimal profit = BigDecimal.ZERO;
				if (sell.getRlzTotal() != null && buy.getRlzTotal() != null) {
					profit = sell.getRlzTotal().subtract(buy.getRlzTotal());
				}
				algo.setStatus(AlgoStatus.DONE);
				algo.setProfit(profit);

				if (algo.getRepeated().equals(TrueFalse.TRUE)) {
					algo.setDescr("DONE_AND_REPEATED (" + tick.getLast() + ")");
					updateAlgoStatus(algo);
					repeatAlgo(user, algo);
					logger.info(algoLogger(algo));
				} else {
					algo.setDescr("DONE_AND_EXPIRED (" + tick.getLast() + ")");
					updateAlgoStatus(algo);
					logger.info(algoLogger(algo));
				}

				MailUtils.sendEmailToUser(algo.getUserId(), "Algo Durumu #" + algo.getId(),
						"Satış gerçekleşti. Tahmini kar: " + profit + " TL"  + getAlgoInfoForMail(algo));

				break;
			default:
				algo.setStatus(AlgoStatus.ERROR);
				algo.setDescr("Unknown sell status\"! (" + sell.getStatus() + ")");
				updateAlgoStatus(algo);
				logger.error(algoLogger(algo));

				break;
			}
		}
	}

	private void updateAlgo(Algo algo) {
		if (!AlgoDao.updateAlgo(algo)) {
			logger.error("Algo update error!");
		}
		;
	}

	private void updateAlgoStatus(Algo algo) {
		if (!AlgoDao.updateAlgoStatus(algo)) {
			logger.error("Algo update status error!" + algo.toString());
		}
		;
	}

	private void updateAlgoBuyId(Algo algo) {
		if (!AlgoDao.updateBuyId(algo)) {
			logger.error("Algo update buy Id error!" + algo.toString());
		}
		;
	}

	private void updateAlgoSellId(Algo algo) {
		if (!AlgoDao.updateSellId(algo)) {
			logger.error("Algo update sell Id error!" + algo.toString());
		}
		;
	}

	private void executeDefault(Algo algo) {
		algo.setStatus(AlgoStatus.ERROR);
		algo.setDescr("Unknown algo status to execute! " + algo.getStatus());
		updateAlgoStatus(algo);
		logger.info(algoLogger(algo));
	}

	private void repeatAlgo(User user, Algo algo) {
		try {
			int refId = (algo.getRefId() > 0) ? algo.getRefId() : algo.getId(); // orjinal algonun id sini geç
			Result r = createAlgo(user, algo.getName(), algo.getPairSynbol(), algo.getMaxAmount(), algo.getBuyRate(),
					algo.getSellRate(), algo.getTrigRate(), algo.getRepeated(), algo.getDuration(), refId,
					algo.getPriceDuration(), algo.getBasePrice());
			if (r != null && r.isSuccess()) {
				logger.debug("#" + algo.getId() + " algo repeated");
			} else {
				logger.error("#" + algo.getId() + " algo repeate create error!");
			}
		} catch (Exception e) {
			logger.error("error here", e);
		}

		/*
		 * BigDecimal avgPrice = TickDao.getAvgPrice(algo); Date createDate =
		 * AlgoServer.getAlgoDate();
		 * 
		 * algo.setLastCreate(createDate);
		 * algo.setExpireDate(DateUtils.getNextPeriod(createDate, algo.getDuration()));
		 * algo.setBuyPrice(avgPrice.multiply(algo.getBuyRate()));
		 * algo.setTrigPrice(avgPrice.multiply(algo.getTrigRate()));
		 * algo.setSellPrice(avgPrice.multiply(algo.getSellRate()));
		 * algo.setAvgPrice(avgPrice); algo.setStatus(AlgoStatus.NEW);
		 * algo.setDescr("REPEATED"); algo.setBuyId(0); algo.setSellId(0);
		 * updateAlgo(algo); logger.info(algoLogger(1, algo));
		 */
	}

	public Result createAlgo(User user, String name, PairSymbol pairSymbol, BigDecimal maxAmount, BigDecimal buyRate,
			BigDecimal sellRate, BigDecimal trigRate, TrueFalse repeated, int duration, int refId, int priceDuration,
			BigDecimal basePrice)
			throws Exception {

		Result result = new Result(true);
		Algo algo = null;
		BigDecimal avgPrice = null;
		

		try {
			
			// TODO User check;
			UserAlgoLimits limit = UserDao.getUserAlgoLimits(user);
			if (limit == null) {
				result.setSuccess(false);
				result.setErrorMsg("Limit bilgileri alınamadı!");
			}
			
			if (result.isSuccess() && limit.getMaxAlgo().subtract(limit.getOpenAlgo()).compareTo(BigDecimal.ZERO) <= 0) {
				result.setSuccess(false);
				result.setErrorMsg("Açık algo sayısı limitini aştığınız için yeni algo oluşturamıyoruz!");	
			}
	
			if (result.isSuccess() && limit.getMaxAmt().subtract(limit.getOpenAmt()).compareTo(maxAmount) <= 0) {
				result.setSuccess(false);
				result.setErrorMsg("Açık algo tutarı limitini aştığınız için yeni algo oluşturamıyoruz!");	
			}
			
			if (result.isSuccess()){
				algo = new Algo();
				Date createDate = AlgoServer.getAlgoDate();
				algo.setName(name);
				algo.setPairSynbol(pairSymbol);
				algo.setCreateDate(createDate);
				algo.setLastCreate(createDate);
				algo.setExpireDate(DateUtils.getNextPeriod(createDate, duration));
				algo.setMaxAmount(maxAmount);
				algo.setBasePrice(basePrice);
	
				algo.setBuyRate(buyRate);
				algo.setPriceDuration(priceDuration);
	
				algo.setSellRate(sellRate);
				algo.setTrigRate(trigRate);
	
				algo.setRepeated(repeated);
				algo.setRefId(refId);
				algo.setStatus(AlgoStatus.NEW);
				algo.setDuration(duration);
				algo.setDescr("CREATED.");
				algo.setUserId(user.getId());
				
				// önyüzden bir fiyat belirlenmişse ortalama olarak o fiyatı al. Aksi halde ortalaa fiyatı bul.
				if (algo.getBasePrice() != null) {
					avgPrice = algo.getBasePrice();
				} else {
					avgPrice = TickDao.getAvgPrice(algo);
					if (avgPrice == null) {
						result.setSuccess(false);
						result.setErrorMsg("Algo için fiyat hesaplanamadı!");	
					} 
				}
			}
			
			if (result.isSuccess() 
					&& (!(algo.getBuyRate().compareTo(algo.getSellRate()) < 0
					&& algo.getSellRate().compareTo(algo.getTrigRate()) <= 0))) {
				result.setSuccess(false);
				result.setErrorMsg("Alış Fiyatı < Satış Fiyatı <= Tetik Fiyatı olmalıdır!");	
			}

			if (result.isSuccess()){
				algo.setBuyPrice(avgPrice.multiply(algo.getBuyRate()));
				algo.setSellPrice(avgPrice.multiply(algo.getSellRate()));
				algo.setTrigPrice(avgPrice.multiply(algo.getTrigRate()));
				algo.setAvgPrice(avgPrice);
	
				// TODO public/private declared? + customer_role
				
				if (AlgoDao.insertAlgo(algo)) {
					result.setSuccess(true);
				} else {
					result.setSuccess(false);
					result.setErrorMsg("Algo kaydedilemedi");	
				}			
			}
		
		} catch (Exception e) {
			result.setSuccess(false);
			result.setErrorMsg("Algo kaydedilemedi");	
			e.printStackTrace();
		} 

		return result;
	}

	public boolean retryAlgo(int algoId) {
		boolean result = false;

		// TODO user check
		Algo algo = AlgoDao.getAlgo(algoId);
		if (algo != null && algo.getStatus() == AlgoStatus.ERROR && algo.getBuyId() == 0) {
			algo.setStatus(AlgoStatus.NEW);
			algo.setDescr("ALGO RETRIED");
			if (AlgoDao.updateAlgo(algo)) {
				logger.debug("algo retired: " + algo.toString());
				result = true;
			} else {
				result = false;
			}
		}

		return result;
	}

	public Result cancelAlgo(int algoId) {
		Result result = new Result(false);

		try {
			Algo algo = AlgoDao.getAlgo(algoId);
			if (algo == null) {
				result.setErrorCode("10");
				result.setErrorMsg("Algo not found !");
			} else {
				if (algo.getStatus() != AlgoStatus.SELL_OPEN) {
					OrderHelper h = new OrderHelper();
					AlgoHelper a = new AlgoHelper();

					Order o = OrderDao.getOrder(algo.getBuyId());
					if (algo.getBuyId() > 0 && o.getStatus() == OrderStatus.OPEN) {
						// alış emri var ve açık ise iptal et.

						if (h.cancelOrder(o)) {
							algo.setStatus(AlgoStatus.CANCEL);
							algo.setDescr("CANCELED BY USER");
							// algo.setBuyId(0);
							a.updateAlgo(algo);
							result.setSuccess(true);
						} else {
							result.setErrorCode("10");
							result.setErrorMsg("Buy order cancel error!");
						}
					} else {
						algo.setStatus(AlgoStatus.CANCEL);
						algo.setDescr("CANCELED BY USER");
						a.updateAlgo(algo);
						result.setSuccess(true);
					}
				} else {
					result.setErrorCode("20");
					result.setErrorMsg("Algo status must be different than SELL OPEN!");
				}
			}
		} catch (Exception e) {
			result.setErrorCode("-1");
			result.setErrorMsg("Exception: " + e.getMessage());
		}

		return result;
	}

	private String algoLogger(Algo a) {
		return new StringBuffer().append("#").append(a.getId()).append(" ").append(a.getDescr()).toString();
	}

}
