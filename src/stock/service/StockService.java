package stock.service;

import stock.dao.AccountDAO;
import stock.dao.StockDAO;
import stock.dao.HoldingDAO;
import stock.dao.OrderDAO;
import stock.dto.*;
import stock.util.DBUtil;

public class StockService {

	AccountDAO accDAO = new AccountDAO();
	StockDAO stockDAO = new StockDAO();
	HoldingDAO holdingDAO = new HoldingDAO();
	OrderDAO orderDAO = new OrderDAO();


	// 매수

	public int buyStock(int accountId, int stockId, int qty, int userId) throws Exception {

		Account acc = accDAO.getAccount(accountId, userId);
		if (acc == null)
			return -2; // 내 계좌가 아님

		Stock stock = stockDAO.getStockById(stockId);
		if (stock == null)
			return -1; // 종목 없음

		int totalPrice = stock.getCurrentPrice() * qty;

		// 잔액 체크
		if (acc.getBalance() < totalPrice)
			return -3;

		// 잔액 차감
		accDAO.updateBalance(accountId, acc.getBalance() - totalPrice);

		// 보유 종목 체크
		Holding holding = holdingDAO.getHolding(accountId, stockId);

		if (holding == null) {
			// 신규 매수 → 신규 보유 추가
			holdingDAO.insertHolding(accountId, stockId, qty, stock.getCurrentPrice());
		} else {
			// 기존 보유 → 평단 재계산
			int totalQty = holding.getQuantity() + qty;
			int avgPrice = ((holding.getQuantity() * holding.getAvgPrice()) + totalPrice) / totalQty;

			holdingDAO.updateHolding(holding.getHoldingId(), totalQty, avgPrice);
		}

		// 주문 기록 저장
		orderDAO.insertOrder(accountId, stockId, qty, stock.getCurrentPrice(), "BUY");

		return 1;
	}


	// 매도

	public int sellStock(int accountId, int stockId, int qty, int userId) throws Exception {

		Account acc = accDAO.getAccount(accountId, userId);
		if (acc == null)
			return -2;

		Stock stock = stockDAO.getStockById(stockId);
		if (stock == null)
			return -1;

		Holding holding = holdingDAO.getHolding(accountId, stockId);
		if (holding == null)
			return -3; // 보유 없음

		// 보유 수량 부족
		if (holding.getQuantity() < qty)
			return -4;

		int totalPrice = stock.getCurrentPrice() * qty;

		// 잔액 증가
		accDAO.updateBalance(accountId, acc.getBalance() + totalPrice);

		// 보유 수량 감소
		int remainQty = holding.getQuantity() - qty;

		if (remainQty == 0) {
			holdingDAO.deleteHolding(holding.getHoldingId()); // 완전 비었으면 삭제
		} else {
			// 평단 그대로 유지
			holdingDAO.updateHolding(holding.getHoldingId(), remainQty, holding.getAvgPrice());
		}

		// 주문 기록 저장
		orderDAO.insertOrder(accountId, stockId, qty, stock.getCurrentPrice(), "SELL");

		return 1;
	}
}
