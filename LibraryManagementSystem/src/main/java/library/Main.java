package library;

import library.model.*;
import library.service.LibraryService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    static LibraryService service = new LibraryService();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== University Library System (SE1_P_2 Maven) ===");
        while (true) {
            System.out.println("\n1) Student\n2) Guest\n3) Librarian\n4) Admin\n0) Exit");
            System.out.print("Choose: ");
            String ch = scanner.nextLine().trim();
            switch (ch) {
                case "1": studentMenu(); break;
                case "2": guestMenu(); break;
                case "3": librarianMenu(); break;
                case "4": adminMenu(); break;
                case "0": System.out.println("Bye."); return;
                default: System.out.println("Invalid."); break;
            }
        }
    }

    // student menu (register/login + actions)
    static void studentMenu() {
        System.out.println("-- Student Menu --\n1) Register 2) Login 0) Back");
        String c = scanner.nextLine().trim();
        if (c.equals("1")) {
            System.out.print("Username: "); String u = scanner.nextLine().trim();
            System.out.print("Password: "); String p = scanner.nextLine().trim();
            boolean ok = service.registerStudent(u,p);
            System.out.println(ok ? "Registered." : "Username exists.");
            return;
        } else if (c.equals("2")) {
            System.out.print("Username: "); String u = scanner.nextLine().trim();
            System.out.print("Password: "); String p = scanner.nextLine().trim();
            Student s = service.loginStudent(u,p);
            if (s == null) { System.out.println("Login failed."); return; }
            if (!s.isActive()) { System.out.println("Account deactivated."); return; }
            studentActions(s);
        }
    }

    static void studentActions(Student s) {
        while (true) {
            System.out.println("\n1) Search 2) Request borrow 3) My history 0) Logout");
            String ch = scanner.nextLine().trim();
            if (ch.equals("1")) {
                System.out.print("Title (or enter): "); String t = scanner.nextLine();
                System.out.print("Author (or enter): "); String a = scanner.nextLine();
                System.out.print("Year (or enter): "); String ys = scanner.nextLine();
                Integer y = ys.trim().isEmpty() ? null : Integer.parseInt(ys);
                List<?> res = service.searchBooks(t,a,y);
                res.forEach(System.out::println);
            } else if (ch.equals("2")) {
                System.out.print("Book ID: "); String id = scanner.nextLine().trim();
                System.out.print("Start(YYYY-MM-DD): "); String sd = scanner.nextLine().trim();
                System.out.print("End(YYYY-MM-DD): "); String ed = scanner.nextLine().trim();
                try {
                    service.requestBorrow(s.getUsername(), id, LocalDate.parse(sd), LocalDate.parse(ed));
                    System.out.println("Requested.");
                } catch (Exception ex) { System.out.println("Invalid date."); }
            } else if (ch.equals("3")) {
                Map<String,Object> r = service.studentHistoryReport(s.getUsername());
                System.out.println("Total: "+r.get("total") + ", NotReturned: "+r.get("notReturned") + ", Late: "+r.get("lateCount"));
                @SuppressWarnings("unchecked")
                List<BorrowRequest> his = (List<BorrowRequest>) r.get("history");
                his.forEach(System.out::println);
            } else if (ch.equals("0")) return;
        }
    }

    static void guestMenu() {
        System.out.println("-- Guest --");
        System.out.println("Total students: "+service.totalStudents());
        System.out.print("Search title (or enter): ");
        String t = scanner.nextLine();
        if (!t.trim().isEmpty()) service.searchBooks(t,null,null).forEach(System.out::println);
        System.out.println("Stats -> Students:"+service.totalStudents()+", Books:"+service.totalBooks()+", Requests:"+service.totalRequests()+", CurrentlyBorrowed:"+service.totalCurrentlyBorrowed());
    }

    static void librarianMenu() {
        System.out.print("Username: "); String u = scanner.nextLine().trim();
        System.out.print("Password: "); String p = scanner.nextLine().trim();
        Librarian l = service.loginLibrarian(u,p);
        if (l == null) { System.out.println("Login failed."); return; }
        System.out.println("Welcome "+l.getUsername());

        while (true) {
            System.out.println("\n1) Change pass 2) Add book 3) Edit book 4) Approve requests 5) Record receive 6) Record return 7) Student history 8) Activate/Deactivate 0) Logout");
            String ch = scanner.nextLine().trim();
            try {
                if (ch.equals("1")) {
                    System.out.print("New pass: "); String np = scanner.nextLine().trim(); service.changeLibrarianPassword(l,np); System.out.println("Updated");
                } else if (ch.equals("2")) {
                    System.out.print("ID: "); String id = scanner.nextLine().trim();
                    System.out.print("Title: "); String t = scanner.nextLine().trim();
                    System.out.print("Author: "); String a = scanner.nextLine().trim();
                    System.out.print("Year: "); int y = Integer.parseInt(scanner.nextLine().trim());
                    service.addBook(new Book(id,t,a,y), l); System.out.println("Added");
                } else if (ch.equals("3")) {
                    System.out.print("Book ID: "); String id = scanner.nextLine().trim();
                    Book b = service.getBookById(id);
                    if (b == null) { System.out.println("Not found."); continue; }
                    System.out.print("New title (enter to skip): "); 
                    String nt = scanner.nextLine().trim();
                    System.out.print("New author (enter to skip): "); 
                    String na = scanner.nextLine().trim();
                    System.out.print("New year (enter to skip): "); 
                    String ny = scanner.nextLine().trim();

                    // فقط اگر کاربر چیزی وارد کرده باشد، مقدار جدید اعمال شود
                    if (!nt.isEmpty()) b.setTitle(nt);
                    if (!na.isEmpty()) b.setAuthor(na);
                    if (!ny.isEmpty()) {
                    try {
                        int y = Integer.parseInt(ny);
                        b.setYear(y);
                    } catch (NumberFormatException e) {
                    System.out.println("Invalid year, skipped.");
                    }
}

System.out.println("Edit done.");

                } else if (ch.equals("4")) {
                    List<BorrowRequest> pend = service.getRequestsForApproval();
                    if (pend.isEmpty()) System.out.println("No pending");
                    else {
                        for (int i=0;i<pend.size();i++) System.out.println((i+1)+") "+pend.get(i));
                        System.out.print("Index to approve (0 skip): "); int idx = Integer.parseInt(scanner.nextLine().trim());
                        if (idx>0 && idx<=pend.size()) {
                            boolean ok = service.approveRequest(pend.get(idx-1), l);
                            System.out.println(ok ? "Approved" : "Failed");
                        }
                    }
                } else if (ch.equals("5")) {
                    System.out.print("Student username: "); String su = scanner.nextLine().trim();
                    List<BorrowRequest> cand = service.getAllRequests().stream().filter(r->r.getStudentUsername().equals(su) && r.isApproved() && !r.isReturned()).toList();
                    if (cand.isEmpty()) { System.out.println("No approved pending"); continue; }
                    for (int i=0;i<cand.size();i++) System.out.println((i+1)+") "+cand.get(i));
                    System.out.print("Index to record receiving: "); int ix = Integer.parseInt(scanner.nextLine().trim());
                    if (ix>0 && ix<=cand.size()) {
                        System.out.print("Receive date(YYYY-MM-DD or enter today): "); String rd = scanner.nextLine().trim();
                        LocalDate receive = rd.trim().isEmpty() ? LocalDate.now() : LocalDate.parse(rd);
                        boolean ok = service.recordReceiving(cand.get(ix-1), l, receive);
                        System.out.println(ok ? "Recorded" : "Failed");
                    }
                } else if (ch.equals("6")) {
                    System.out.print("Student username: "); String su = scanner.nextLine().trim();
                    List<BorrowRequest> cand = service.getAllRequests().stream().filter(r->r.getStudentUsername().equals(su) && r.isApproved() && !r.isReturned()).toList();
                    if (cand.isEmpty()) { System.out.println("None to return"); continue; }
                    for (int i=0;i<cand.size();i++) System.out.println((i+1)+") "+cand.get(i));
                    System.out.print("Index to record return: "); int ix = Integer.parseInt(scanner.nextLine().trim());
                    if (ix>0 && ix<=cand.size()) {
                        System.out.print("Return date(YYYY-MM-DD or enter today): "); String rd = scanner.nextLine().trim();
                        LocalDate ret = rd.trim().isEmpty() ? LocalDate.now() : LocalDate.parse(rd);
                        boolean ok = service.recordReturn(cand.get(ix-1), ret);
                        System.out.println(ok ? "Recorded" : "Failed");
                    }
                } else if (ch.equals("7")) {
                    System.out.print("Student username: "); String su = scanner.nextLine().trim();
                    Map<String,Object> rep = service.studentHistoryReport(su);
                    System.out.println("Total: "+rep.get("total")+", NotReturned:"+rep.get("notReturned") + ", Late:"+rep.get("lateCount"));
                } else if (ch.equals("8")) {
                    System.out.print("Student username: "); String su = scanner.nextLine().trim();
                    List<Student> studs = service.getAllStudents();
                    Student st = studs.stream().filter(x->x.getUsername().equals(su)).findFirst().orElse(null);
                    if (st==null) { System.out.println("Not found"); continue; }
                    System.out.println("Current active: "+st.isActive());
                    System.out.print("Set active? (yes/no): "); String yn = scanner.nextLine().trim().toLowerCase();
                    boolean set = yn.startsWith("y");
                    st.setActive(set);
                    System.out.println("Updated");
                } else if (ch.equals("0")) break;
            } catch (Exception ex) {
                System.out.println("Error: "+ex.getMessage());
            }
        }
    }

    static void adminMenu() {
        System.out.print("Admin username: "); String u = scanner.nextLine().trim();
        System.out.print("Password: "); String p = scanner.nextLine().trim();
        Admin a = service.admin; // default admin
        if (!a.login(u,p)) { System.out.println("Auth failed"); return; }
        while (true) {
            System.out.println("\n1) Create librarian 2) View librarian performance 3) Borrow stats 4) Students stats & top10 0) Logout");
            String ch = scanner.nextLine().trim();
            if (ch.equals("1")) {
                System.out.print("Username: "); String lu = scanner.nextLine().trim();
                System.out.print("Password: "); String lp = scanner.nextLine().trim();
                service.addLibrarian(lu, lp); System.out.println("Added");
            } else if (ch.equals("2")) {
                System.out.print("Librarian username: "); String lu = scanner.nextLine().trim();
                System.out.println(service.librarianPerformance(lu));
            } else if (ch.equals("3")) {
                System.out.println(service.borrowStats());
            } else if (ch.equals("4")) {
                System.out.println(service.studentsStatsAndTopLate10());
            } else if (ch.equals("0")) break;
        }
    }
}
