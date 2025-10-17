package library;

import library.model.*;
import library.service.LibraryService;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LibraryService lib = new LibraryService();
        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.println("\n--- Library System ---");
            System.out.println("1. Student");
            System.out.println("2. Guest");
            System.out.println("3. Librarian");
            System.out.println("4. Admin");
            System.out.println("0. Exit");
            int choice = sc.nextInt(); sc.nextLine();

            switch(choice) {
                case 1: studentMenu(lib, sc); break;
                case 2: guestMenu(lib, sc); break;
                case 3: librarianMenu(lib, sc); break;
                case 4: adminMenu(lib, sc); break;
                case 0: sc.close(); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    static void studentMenu(LibraryService lib, Scanner sc) {
        System.out.println("1.Register 2.Login"); int ch = sc.nextInt(); sc.nextLine();
        if(ch==1) {
            System.out.print("Username: "); String u = sc.nextLine();
            System.out.print("Password: "); String p = sc.nextLine();
            if(lib.registerStudent(u,p)) System.out.println("Registered!");
            else System.out.println("Username exists!");
        } else if(ch==2) {
            System.out.print("Username: "); String u = sc.nextLine();
            System.out.print("Password: "); String p = sc.nextLine();
            Student s = lib.loginStudent(u,p);
            if(s!=null) {
                System.out.println("Welcome "+s.getUsername());
                System.out.print("Search book by title (or empty): "); String t = sc.nextLine();
                List<Book> res = lib.searchBooks(t,null,null);
                res.forEach(System.out::println);
                if(!res.isEmpty()) {
                    System.out.print("Request borrow ID: "); String id = sc.nextLine();
                    lib.requestBorrow(s.getUsername(), id, LocalDate.now(), LocalDate.now().plusDays(7));
                    System.out.println("Requested!");
                }
            } else System.out.println("Login failed.");
        }
    }

    static void guestMenu(LibraryService lib, Scanner sc) {
        System.out.println("Total Students: "+lib.totalStudents());
        System.out.print("Search book by title: "); String t = sc.nextLine();
        lib.searchBooks(t,null,null).forEach(System.out::println);
    }

    static void librarianMenu(LibraryService lib, Scanner sc) {
        System.out.print("Username: "); String u = sc.nextLine();
        System.out.print("Password: "); String p = sc.nextLine();
        Librarian l = lib.loginLibrarian(u,p);
        if(l==null) { System.out.println("Login failed."); return; }
        System.out.println("Welcome "+l.getUsername());
        System.out.print("Add book ID: "); String id = sc.nextLine();
        System.out.print("Title: "); String t = sc.nextLine();
        System.out.print("Author: "); String a = sc.nextLine();
        System.out.print("Year: "); int y = sc.nextInt(); sc.nextLine();
        lib.addBook(new Book(id,t,a,y));
        System.out.println("Book added!");
        // تایید درخواست‌ها
        List<BorrowRequest> reqs = lib.getRequestsForApproval();
        for(BorrowRequest r : reqs) { lib.approveRequest(r); System.out.println("Approved "+r.getBookId()); }
    }

    static void adminMenu(LibraryService lib, Scanner sc) {
        System.out.print("Add Librarian Username: "); String u = sc.nextLine();
        System.out.print("Password: "); String p = sc.nextLine();
        lib.addLibrarian(u,p); System.out.println("Librarian added!");
    }
}
