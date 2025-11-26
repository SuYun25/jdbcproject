package stock.controller;

import java.util.ArrayList;
import java.util.Scanner;

import stock.dao.*;
import stock.dto.*;
import stock.service.StockService;
import stock.util.InputUtil;

public class Main {

	public static void main(String[] args) throws Exception {

		Scanner sc = new Scanner(System.in);
		UserDAO userDAO = new UserDAO();

		while (true) {
			System.out.println("===== 메인 메뉴 =====");
			System.out.println("1. 회원가입");
			System.out.println("2. 로그인");
			System.out.println("0. 종료");


			  int menu = InputUtil.getInt(sc, "선택 >> ");

			if (menu == 1) {

				String username = InputUtil.getString(sc, "아이디 입력: ");
				String pw = InputUtil.getString(sc, "비밀번호 입력: ");

				User user = new User();
				user.setUsername(username);
				user.setPassword(pw);

				int result = userDAO.signup(user);
				System.out.println(result > 0 ? "회원가입 성공!" : "회원가입 실패!");

			} else if (menu == 2) {

				System.out.print("아이디 입력: ");
				String username = sc.nextLine();
				System.out.print("비밀번호 입력: ");
				String pw = sc.nextLine();

				User user = userDAO.login(username, pw);

				if (user != null) {
					System.out.println("\n" + user.getUsername() + "님 로그인 성공!");
					loginMenu(sc, user);
				} else {
					System.out.println("로그인 실패!");
				}

			} else if (menu == 0) {
				System.out.println("프로그램 종료");
				break;
			}
		}

		sc.close();
	}
	public static int inputInt(Scanner sc, String message) {
	    while (true) {
	        try {
	            System.out.print(message);
	            return Integer.parseInt(sc.nextLine());
	        } catch (Exception e) {
	            System.out.println("숫자만 입력 가능합니다. 다시 시도하세요.");
	        }
	    }
	}


	// 로그인 후 메뉴
	 private static void loginMenu(Scanner sc, User user) throws Exception {

	        AccountDAO accDAO = new AccountDAO();

	        while (true) {

	            System.out.println("\n===== 계좌 메뉴 =====");
	            System.out.println("1. 계좌 개설");
	            System.out.println("2. 내 계좌 조회");
	            System.out.println("3. 계좌 삭제");
	            System.out.println("4. 계좌 이체");
	            System.out.println("5. 잔액 충전");
	            System.out.println("6. 주식 매매");
	            System.out.println("0. 로그아웃");

	            int sel = InputUtil.getInt(sc, "선택 >> ");

	            // 1. 계좌 개설
	            if (sel == 1) {
	                int accId = InputUtil.getInt(sc, "생성할 계좌번호 입력: ");
	                int result = accDAO.createAccount(user.getUserId(), accId);
	                System.out.println(result > 0 ? "계좌 개설 완료!" : "계좌 개설 실패");
	            }

	            // 2. 계좌 조회
	            else if (sel == 2) {
	                var list = accDAO.getAccounts(user.getUserId());
	                System.out.println("\n===== 내 계좌 목록 =====");
	                for (var a : list) {
	                    System.out.println("계좌번호: " + a.getAccountId() + " | 잔고: " +
	                            a.getBalance() + " | 개설일: " + a.getCreateDate());
	                }
	            }

	            // 3. 계좌 삭제
	            else if (sel == 3) {
	                int accId = InputUtil.getInt(sc, "삭제할 계좌번호: ");
	                int result = accDAO.deleteAccount(accId, user.getUserId());
	                System.out.println(result > 0 ? "계좌 삭제 완료!" : "계좌 삭제 실패");
	            }

	            // 4. 계좌 이체
	            else if (sel == 4) {
	                int fromAcc = InputUtil.getInt(sc, "출금할 계좌번호: ");
	                int toAcc = InputUtil.getInt(sc, "입금할 계좌번호: ");
	                int amount = InputUtil.getInt(sc, "이체 금액: ");

	                int result = accDAO.transfer(fromAcc, toAcc, amount, user.getUserId());

	                switch (result) {
	                    case 1 -> System.out.println("이체 완료!");
	                    case -1 -> System.out.println("출금 계좌 오류");
	                    case -2 -> System.out.println("잔액 부족!");
	                    case -3 -> System.out.println("입금 계좌 없음!");
	                    default -> System.out.println("이체 실패!");
	                }
	            }

	            // 5. 충전
	            else if (sel == 5) {
	                int accId = InputUtil.getInt(sc, "충전할 계좌번호: ");
	                int amount = InputUtil.getInt(sc, "충전 금액: ");

	                int result = accDAO.deposit(accId, amount, user.getUserId());
	                System.out.println(result > 0 ? "충전 완료!" : "충전 실패");
	            }

	            // 6. 주식 매매
	            else if (sel == 6) {
	                stockTradeMenu(sc, user);
	            }

	            // 0. 로그아웃
	            else if (sel == 0) {
	                System.out.println("로그아웃 완료");
	                break;
	            }
	        }
	    }

	    // ---------------------------
	    // 주식 매매 메뉴
	    // ---------------------------
	    private static void stockTradeMenu(Scanner sc, User user) throws Exception {

	        AccountDAO accDAO = new AccountDAO();
	        StockDAO stockDAO = new StockDAO();
	        HoldingDAO holdingDAO = new HoldingDAO();
	        StockService stockService = new StockService();

	        stockDAO.updateRandomPrices();
	        System.out.println("\n[알림] 시장 가격이 자동 갱신되었습니다!\n");

	        var accounts = accDAO.getAccounts(user.getUserId());

	        if (accounts.isEmpty()) {
	            System.out.println("계좌가 없습니다. 먼저 계좌를 개설하세요.");
	            return;
	        }

	        System.out.println("\n===== 내 계좌 목록 =====");
	        for (var a : accounts) {
	            System.out.println(a.getAccountId() + " | 잔고: " + a.getBalance());
	        }

	        int accId = InputUtil.getInt(sc, "사용할 계좌 선택 >> ");

	        Account account = accDAO.getAccount(accId, user.getUserId());
	        if (account == null) {
	            System.out.println("잘못된 계좌번호입니다.");
	            return;
	        }

	        System.out.println("\n>>> 선택한 계좌 잔고: " + account.getBalance() + "원");

	        while (true) {

	            System.out.println("\n===== 주식 매매 메뉴 =====");
	            System.out.println("1. 전체 종목 조회");
	            System.out.println("2. 종목 검색");
	            System.out.println("3. 보유 종목 조회");
	            System.out.println("4. 매수");
	            System.out.println("5. 매도");
	            System.out.println("0. 뒤로가기");

	            int sel = InputUtil.getInt(sc, "선택 >> ");

	            // 1. 전체 종목 조회
	            if (sel == 1) {
	                var list = stockDAO.getAllStocks();
	                System.out.println("\n===== 전체 종목 =====");
	                for (var s : list) {
	                    System.out.println(s.getStockName() + " | 현재가: " + s.getCurrentPrice() + "원");
	                }
	            }

	            // 2. 종목 검색
	            else if (sel == 2) {
	                String name = InputUtil.getString(sc, "검색할 종목명: ");
	                var list = stockDAO.searchByName(name);

	                if (list.isEmpty()) System.out.println("검색 결과 없음.");
	                else {
	                    System.out.println("\n===== 검색 결과 =====");
	                    for (var s : list) {
	                        System.out.println(s.getStockName() + " | 현재가: " + s.getCurrentPrice() + "원");
	                    }
	                }
	            }

	            // 3. 보유 종목 조회
	            else if (sel == 3) {

	                var holdings = holdingDAO.getHoldingsByAccount(accId);

	                System.out.println("\n===== 보유 종목 =====");
	                if (holdings.isEmpty()) {
	                    System.out.println("보유 종목 없음");
	                } else {
	                    for (var h : holdings) {
	                        Stock st = stockDAO.getStockById(h.getStockId());
	                        int eval = st.getCurrentPrice() * h.getQuantity();
	                        int cost = h.getAvgPrice() * h.getQuantity();
	                        int profit = eval - cost;

	                        System.out.printf("%s | %d주 | 평단: %d | 현재가: %d | 평가손익: %d\n",
	                                st.getStockName(), h.getQuantity(), h.getAvgPrice(),
	                                st.getCurrentPrice(), profit);
	                    }
	                }
	            }

	            // 4. 매수
	            else if (sel == 4) {

	                String name = InputUtil.getString(sc, "매수할 종목명: ");
	                Stock stock = stockDAO.getStockByName(name);

	                if (stock == null) {
	                    System.out.println("존재하지 않는 종목입니다.");
	                    continue;
	                }

	                System.out.println("현재가: " + stock.getCurrentPrice());
	                int qty = InputUtil.getInt(sc, "매수 수량: ");

	                int result = stockService.buyStock(accId, stock.getStockId(), qty, user.getUserId());

	                System.out.println(result == 1 ? "매수 성공!"
	                        : result == -3 ? "잔액 부족!" : "매수 실패!");
	            }

	            // 5. 매도
	            else if (sel == 5) {

	                String name = InputUtil.getString(sc, "매도할 종목명: ");
	                Stock stock = stockDAO.getStockByName(name);

	                if (stock == null) {
	                    System.out.println("존재하지 않는 종목");
	                    continue;
	                }

	                Holding h = holdingDAO.getHolding(accId, stock.getStockId());
	                if (h == null) {
	                    System.out.println("보유하지 않은 종목");
	                    continue;
	                }

	                System.out.println("보유 수량: " + h.getQuantity());
	                int qty = InputUtil.getInt(sc, "매도 수량: ");

	                int result = stockService.sellStock(accId, stock.getStockId(), qty, user.getUserId());

	                System.out.println(result == 1 ? "매도 성공!"
	                        : result == -4 ? "보유 수량 부족!" : "매도 실패!");
	            }

	            // 0. 뒤로가기
	            else if (sel == 0) break;
	        }
	    }
	}