package ir.ac.kntu.main;

import ir.ac.kntu.modules.*;
import ir.ac.kntu.util.ConsoleStyle;
import ir.ac.kntu.util.Pagination;
import ir.ac.kntu.util.ScannerWrapper;
import ir.ac.kntu.util.SystemProperties;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class AdminMenu {

    private static final String SELECTION = "Please select an option: ";
    private static final String ERROR = "Selection out of bound.";

    private AdminMenu() {
    }

    public static void show(Admin admin) {
        while (true) {
            ConsoleStyle.clearScreen();
            System.out.println(ConsoleStyle.CYAN +
                    "========== ADMIN PANEL ==========" + ConsoleStyle.RESET);
            System.out.println(ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                    "1. Manage Users       \n" +
                    "2. Manage Supporters  \n" +
                    "3. Manage Admins      \n" +
                    "4. see all SystemUsers\n" +
                    "5. System Settings    \n" +
                    "6. Exit               \n" +
                    ConsoleStyle.RESET);

            int select = ScannerWrapper.nextInt(SELECTION);
            switch (select) {
                case 1 -> manageUsers();
                case 2 -> manageSupporters();
                case 3 -> manageAdmins(admin);
                case 4 -> seeAllEntities();
                case 5 -> systemSettings(admin);
                case 6 -> {
                    return;
                }
                default -> {
                    ScannerWrapper.rewritePrompt(ERROR);
                    ScannerWrapper.pause();
                }
            }
        }
    }

    private static void manageUsers(){
        while (true){
            Pagination<NormalUser> pagination = new Pagination<>(LibraryManger.getInstance().getUserById().values().stream().toList());
            ConsoleStyle.clearScreen();
            for (NormalUser user : pagination.getCurrentPage()){
                System.out.println(ConsoleStyle.YELLOW + user + ConsoleStyle.RESET);
            }
            System.out.println(ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                    "-----Page " + pagination.getCurrentPageNumber() + "/" + pagination.gerTotalPageNumber() + "-----\n" +
                    (pagination.hasNextPage() ? "N. Next Page    \n" : "") +
                    (pagination.hasPreviousPage() ? "P. Previous Page\n" : "") +
                            "==================\n" +
                            "1. Search User  \n" +
                            "2. Edit User    \n" +
                            "3. Remove User  \n" +
                            "4. Back         \n" +
                    ConsoleStyle.RESET);

            String select = ScannerWrapper.nextLine(SELECTION);
            switch (select.toLowerCase()){
                case "n" -> pagination.nextPage();
                case "p" -> pagination.previousPage();
                case "1" -> searchUsers();
                case "2" -> editUser();
                case "3" -> removeUser();
                case "4" -> {
                    return;
                }
                default ->{
                    ScannerWrapper.rewritePrompt(ERROR);
                    ScannerWrapper.pause();
                }
            }
        }
    }

    private static void searchUsers() {
        while (true) {
            ConsoleStyle.clearScreen();
            System.out.println(ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                            "Search By     \n" +
                            "1. First Name \n" +
                            "2. Last Name  \n" +
                            "3. Password   \n" +
                            "4. Back       \n" + ConsoleStyle.RESET);

            int select = ScannerWrapper.nextInt(SELECTION);
            try {
                List<Entity> result;
                switch (select) {
                    case 1 -> {
                        String firstName = ScannerWrapper.nextLine("First Name: ");
                        result = LibraryManger.getInstance().filteredSearch(entity -> entity.getFirstName().toLowerCase().contains(firstName.toLowerCase()));
                    }
                    case 2 -> {
                        String lastName = ScannerWrapper.nextLine("Last Name: ");
                        result = LibraryManger.getInstance().filteredSearch(entity -> entity.getLastName().toLowerCase().contains(lastName.toLowerCase()));
                    }
                    case 3 -> {
                        String password = ScannerWrapper.nextLine("Password: ");
                        result = LibraryManger.getInstance().filteredSearch(entity -> entity.getPassword().equals(password));
                    }
                    case 4 -> {
                        return;
                    }
                    default -> {
                        ScannerWrapper.rewritePrompt(ERROR);
                        ScannerWrapper.pause();
                        continue;
                    }
                }
                if (result.isEmpty()) {
                    System.out.println(ConsoleStyle.RED + "No Result Found." + ConsoleStyle.RESET);
                } else {
                    for (Entity entity : result) {
                        System.out.println(ConsoleStyle.YELLOW + entity + ConsoleStyle.RESET);
                    }
                }
            } catch (Exception e) {
                System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
            }
            ScannerWrapper.pause();
        }
    }
    
    private static void editUser() {
        ConsoleStyle.clearScreen();
        try {
            String id = ScannerWrapper.nextLine("User ID: ");
            NormalUser user = LibraryManger.getInstance().getUserById().get(id);
            if (user == null){
                throw new IllegalStateException("User not found.");
            }
            while (true) {
                ConsoleStyle.clearScreen();
                System.out.println(ConsoleStyle.YELLOW + user + ConsoleStyle.RESET + "\n");
                System.out.println(ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                        "Select the property you want to change:\n" +
                                "1. First Name     \n" +
                                "2. Last Name      \n" +
                                "3. Password       \n" +
                        (user.isActive() ? "4. Deactivate User\n" : "4. Activate User  \n") +
                                "5. Back           \n" +
                        ConsoleStyle.RESET);

                int select = ScannerWrapper.nextInt(SELECTION);
                switch (select) {
                    case 1 -> {
                        String value = ScannerWrapper.nextLine("New First Name: ");
                        user.setFirstName(value);
                    }
                    case 2 -> {
                        String value = ScannerWrapper.nextLine("New Last Name: ");
                        user.setLastName(value);
                    }
                    case 3 -> {
                        String value = ScannerWrapper.nextLine("New Password: ");
                        user.setPassword(value);
                    }
                    case 4 -> {
                        user.changeState();
                    }
                    case 5 -> {
                        return;
                    }
                    default -> {
                        ScannerWrapper.rewritePrompt(ERROR);
                        ScannerWrapper.pause();
                        continue;
                    }
                }
                System.out.println(ConsoleStyle.GREEN + "User Updated Successfully." + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            }

        } catch (Exception e) {
            System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
            ScannerWrapper.pause();
        }
    }

    private static void removeUser() {
        ConsoleStyle.clearScreen();
        try {
            String id = ScannerWrapper.nextLine("User ID: ");
            NormalUser user = LibraryManger.getInstance().getUserById().get(id);
            if (user == null){
                throw new IllegalStateException("User not found.");
            }

            String confirm = ScannerWrapper.nextLine("Type YES to confirm: "+ ConsoleStyle.PURPLE + "Leave Empty to return." + ConsoleStyle.RESET);
            if (!confirm.equalsIgnoreCase("YES")) {
                System.out.println(ConsoleStyle.YELLOW + "Operation Cancelled." + ConsoleStyle.RESET);
                ScannerWrapper.pause();
                return;
            }
            LibraryManger.getInstance().removeUser(user);
            System.out.println(ConsoleStyle.GREEN + "User Removed Successfully." + ConsoleStyle.RESET);

        } catch (Exception e) {
            System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
        }
        ScannerWrapper.pause();
    }

    private static void manageSupporters() {
        while (true) {
            Pagination<Supporter> pagination = new Pagination<>(LibraryManger.getInstance().getSupporterByPassword().values().stream().toList());
            ConsoleStyle.clearScreen();
            for (Supporter supporter : pagination.getCurrentPage()) {
                System.out.println(ConsoleStyle.YELLOW + supporter + ConsoleStyle.RESET);
            }
            System.out.println(ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                    "-------Page " + pagination.getCurrentPageNumber() + "/" + pagination.gerTotalPageNumber() + "-------\n" +
                    (pagination.hasNextPage() ? "N. Next Page        \n" : "") +
                    (pagination.hasPreviousPage() ? "P. Previous Page    \n" : "") +
                            "======================\n" +
                            "1. Add Supporter    \n" +
                            "2. Edit Supporter   \n" +
                            "3. Remove Supporter \n" +
                            "4. Departments      \n" +
                            "5. Back             \n" + ConsoleStyle.RESET);

            String select = ScannerWrapper.nextLine(SELECTION);
            switch (select) {
                case "1" -> addSupporter();
                case "2" -> editSupporter();
                case "3" -> removeSupporter();
                case "4" -> supporterDepartments();
                case "5" -> {
                    return;
                }
                default -> {
                    ScannerWrapper.rewritePrompt(ERROR);
                    ScannerWrapper.pause();
                }
            }
        }
    }

    private static void addSupporter() {
        ConsoleStyle.clearScreen();
        System.out.println(ConsoleStyle.BRIGHT_PURPLE + "Leave all fields empty to exit\n\n" + ConsoleStyle.RESET);
        try {
            String first = ScannerWrapper.nextLine("First Name: ");
            String last = ScannerWrapper.nextLine("Last Name: ");
            String username = ScannerWrapper.nextLine("Username: ");
            String password = ScannerWrapper.nextLine("Password: ");
            if (first.isBlank() && last.isBlank() && username.isBlank() && password.isBlank()){
                return;
            }
            Set<Department> departments = chooseDepartments();

            Supporter supporter = new Supporter(first, last, username, password, departments);
            LibraryManger.getInstance().addSupporter(supporter);
            System.out.println(ConsoleStyle.GREEN + "Supporter Created Successfully." + ConsoleStyle.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
        }
        ScannerWrapper.pause();
    }

    private static Set<Department> chooseDepartments(){
        System.out.println();
        System.out.println(
                """
                        1. REPORT_PROBLEM
                        2. REQUEST_ITEM
                        3. FINANCIAL_AFFAIRS
                        4. RESERVE_ITEM""");
        Set<Department> ans = new HashSet<>();
        String select = ScannerWrapper.nextLine("Choose Departments(you can choose multiple options, use \",\" as seperator): ");
        if (!select.trim().matches("^[1-4](\\s*,\\s*[1-4])*$")){
            throw new IllegalArgumentException("Invalid selection");
        }
        if (select.contains("1")){
            ans.add(Department.REPORT_PROBLEM);
        }
        if (select.contains("2")){
            ans.add(Department.REQUEST_ITEM);
        }
        if (select.contains("3")){
            ans.add(Department.FINANCIAL_AFFAIRS);
        }
        if (select.contains("4")){
            ans.add(Department.RESERVE_ITEM);
        }
        return ans;
    }

    private static void removeSupporter() {
        ConsoleStyle.clearScreen();
        try {
            String password = ScannerWrapper.nextLine("Supporter Password: ");
            Supporter supporter = LibraryManger.getInstance().getSupporterByPassword().get(password);
            if (supporter == null){
                throw new IllegalStateException("Supporter not found.");
            }

            String confirm = ScannerWrapper.nextLine("Type YES to confirm: "+ ConsoleStyle.PURPLE + "Leave Empty to return." + ConsoleStyle.RESET);
            if (!confirm.equalsIgnoreCase("YES")) {
                System.out.println(ConsoleStyle.YELLOW + "Operation Cancelled." + ConsoleStyle.RESET);
                ScannerWrapper.pause();
                return;
            }
            LibraryManger.getInstance().removeSupporter(supporter);
            System.out.println(ConsoleStyle.GREEN + "Supporter Removed Successfully." + ConsoleStyle.RESET);

        } catch (Exception e) {
            System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
        }
        ScannerWrapper.pause();
    }

    private static void editSupporter() {
        ConsoleStyle.clearScreen();
        try {
            String password = ScannerWrapper.nextLine("Supporter Password: ");
            Supporter supporter = LibraryManger.getInstance().getSupporterByPassword().get(password);
            if (supporter == null){
                throw new IllegalStateException("Supporter not found.");
            }
            while (true) {
                ConsoleStyle.clearScreen();
                System.out.println(ConsoleStyle.YELLOW + supporter + ConsoleStyle.RESET + "\n");
                System.out.println(ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                        "Select the property you want to change:\n" +
                                "1. First Name \n" +
                                "2. Last Name  \n" +
                                "3. Username   \n" +
                                "4. Password   \n" +
                        (supporter.isActive() ? "5. Deactivate \n" : "5. Activate   \n") +
                                "6. Back       \n" + ConsoleStyle.RESET);

                int select = ScannerWrapper.nextInt(SELECTION);
                switch (select) {
                    case 1 -> {
                        String value = ScannerWrapper.nextLine("New First Name: ");
                        supporter.setFirstName(value);
                    }
                    case 2 -> {
                        String value = ScannerWrapper.nextLine("New Last Name: ");
                        supporter.setLastName(value);
                    }
                    case 3 -> {
                        String value = ScannerWrapper.nextLine("New Username: ");
                        supporter.setUserName(value);
                    }
                    case 4 -> {
                        String value = ScannerWrapper.nextLine("New Password: ");
                        LibraryManger.getInstance().getSupporterByPassword().remove(supporter.getPassword());
                        supporter.setPassword(value);
                        LibraryManger.getInstance().getSupporterByPassword().put(supporter.getPassword(), supporter);
                    }
                    case 5 ->{
                        supporter.changeState();
                    }
                    case 6 -> {
                        return;
                    }
                    default -> {
                        ScannerWrapper.rewritePrompt(ERROR);
                        ScannerWrapper.pause();
                        continue;
                    }
                }
                System.out.println(ConsoleStyle.GREEN + "Supporter Updated Successfully." + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            }
        } catch (Exception e){
            System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
            ScannerWrapper.pause();
        }
    }

    private static Department chooseDepartment(){
        System.out.println();
        System.out.println(
                """
                        1. REPORT_PROBLEM
                        2. REQUEST_ITEM
                        3. FINANCIAL_AFFAIRS
                        4. RESERVE_ITEM""");

        int select = ScannerWrapper.nextInt("Department: ");
        return switch (select){
            case 1 -> Department.REPORT_PROBLEM;
            case 2 -> Department.REQUEST_ITEM;
            case 3 -> Department.FINANCIAL_AFFAIRS;
            case 4 -> Department.RESERVE_ITEM;
            default -> throw new IllegalArgumentException("Invalid Department");
        };
    }

    private static void supporterDepartments(){
        ConsoleStyle.clearScreen();
        try{
            String password = ScannerWrapper.nextLine("Supporter Password: ");
            Supporter supporter = LibraryManger.getInstance().getSupporterByPassword().get(password);
            if (supporter == null){
                throw new IllegalStateException("Supporter not found.");
            }
            while(true){
                ConsoleStyle.clearScreen();
                System.out.println(ConsoleStyle.BOLD + "Current Departments:" + ConsoleStyle.RESET);
                for (Department department : supporter.getDepartments()){
                    System.out.println(ConsoleStyle.BRIGHT_PURPLE + "- " + department + ConsoleStyle.RESET);
                }
                System.out.println();
                System.out.println(ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                        "1. Add Department    \n" +
                        "2. Remove Department \n" +
                        "3. Back              \n" +
                        ConsoleStyle.RESET);

                int select = ScannerWrapper.nextInt(SELECTION);
                switch(select){
                    case 1 ->{
                        Department department = chooseDepartment();
                        supporter.addDepartment(department);
                        System.out.println(ConsoleStyle.GREEN + "Department Added." + ConsoleStyle.RESET);
                        ScannerWrapper.pause();
                    }
                    case 2 ->{
                        Department department = chooseDepartment();
                        supporter.removeDepartment(department);
                        System.out.println(ConsoleStyle.GREEN + "Department Removed." + ConsoleStyle.RESET);
                        ScannerWrapper.pause();
                    }
                    case 3 ->{
                        return;
                    }
                    default->{
                        ScannerWrapper.rewritePrompt(ERROR);
                        ScannerWrapper.pause();
                    }
                }
            }
        }catch (Exception e){
            System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
            ScannerWrapper.pause();
        }
    }

    private static void manageAdmins(Admin admin){
        while(true){
            Pagination<Admin> pagination = new Pagination<>(LibraryManger.getInstance().getAdminByPassword().values().stream().toList());
            ConsoleStyle.clearScreen();
            for (Admin admin1 : pagination.getCurrentPage()){
                System.out.println(ConsoleStyle.YELLOW + admin1 + ConsoleStyle.RESET);
            }
            System.out.println(ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                    "------Page " + pagination.getCurrentPageNumber() + "/" + pagination.gerTotalPageNumber() + "------\n" +
                    (pagination.hasNextPage() ? "N. Next Page      \n" : "") +
                    (pagination.hasPreviousPage() ? "P. Previous Page  \n" : "") +
                            "==================\n" +
                            "1. Create Admin   \n"+
                            "2. Remove Admin   \n"+
                            "3. Edit Admin     \n"+
                            "4. Back           \n"+ ConsoleStyle.RESET);

            String select = ScannerWrapper.nextLine(SELECTION);
            switch(select.toLowerCase()){
                case "n" -> pagination.nextPage();
                case "p" -> pagination.previousPage();
                case "1" -> createAdmin(admin);
                case "2" -> removeAdmin(admin);
                case "3" -> editAdmin(admin);
                case "4" ->{
                    return;
                }
                default->{
                    ScannerWrapper.rewritePrompt(ERROR);
                    ScannerWrapper.pause();
                }
            }
        }
    }

    private static void createAdmin(Admin creator){
        ConsoleStyle.clearScreen();
        System.out.println(ConsoleStyle.BRIGHT_PURPLE + "Leave all fields empty to exit\n\n" + ConsoleStyle.RESET);
        try{
            String first = ScannerWrapper.nextLine("First Name: ");
            String last = ScannerWrapper.nextLine("Last Name: ");
            String username = ScannerWrapper.nextLine("Username: ");
            String password = ScannerWrapper.nextLine("Password: ");
            Admin admin = new Admin(first, last, username, password, creator);
            if (first.isBlank() && last.isBlank() && username.isBlank() && password.isBlank()){
                return;
            }
            LibraryManger.getInstance().addAdmin(admin);
            System.out.println(ConsoleStyle.GREEN+ "Admin Created Successfully." +ConsoleStyle.RESET);
        }catch(Exception e){
            System.out.println(ConsoleStyle.RED+ e.getMessage() +ConsoleStyle.RESET);
        }
        ScannerWrapper.pause();
    }

    private static void removeAdmin(Admin editor){
        ConsoleStyle.clearScreen();
        try{
            String password = ScannerWrapper.nextLine("Admin Password: ");
            Admin target = LibraryManger.getInstance().getAdminByPassword().get(password);
            if (target == null){
                throw new IllegalStateException("Admin not found.");
            }
            String confirm = ScannerWrapper.nextLine("Type YES to confirm: "+ ConsoleStyle.PURPLE + "Leave Empty to return." + ConsoleStyle.RESET);
            if (!confirm.equalsIgnoreCase("YES")) {
                System.out.println(ConsoleStyle.YELLOW + "Operation Cancelled." + ConsoleStyle.RESET);
                ScannerWrapper.pause();
                return;
            }
            LibraryManger.getInstance().removeAdmin(editor,target);
            System.out.println(ConsoleStyle.GREEN+ "Admin Removed." +ConsoleStyle.RESET);
        }catch(Exception e){
            System.out.println(ConsoleStyle.RED+ e.getMessage() +ConsoleStyle.RESET);
        }
        ScannerWrapper.pause();
    }

    private static void editAdmin(Admin editor){
        ConsoleStyle.clearScreen();
        try{
            String password = ScannerWrapper.nextLine("Admin Password: ");
            Admin target = LibraryManger.getInstance().getAdminByPassword().get(password);
            if (target == null){
                throw new IllegalStateException("Admin not found.");
            }
            if(!editor.canEdit(target)){
                throw new IllegalStateException("You don't have permission to edit this admin.");
            }

            while(true){
                ConsoleStyle.clearScreen();
                System.out.println(ConsoleStyle.YELLOW + target + ConsoleStyle.RESET + "\n");
                System.out.println(ConsoleStyle.BG_WHITE + ConsoleStyle.BLUE +
                        "Select the property you want to change:\n" +
                                "1. First Name \n"+
                                "2. Last Name  \n"+
                                "3. Username   \n"+
                                "4. Password   \n"+
                                "5. Back       \n"+ ConsoleStyle.RESET);

                int select = ScannerWrapper.nextInt(SELECTION);
                switch(select){
                    case 1 ->{
                        target.setFirstName(ScannerWrapper.nextLine("New First Name: "));
                    }
                    case 2 ->{
                        target.setLastName(ScannerWrapper.nextLine("New Last Name: "));
                    }
                    case 3 ->{
                        target.setUserName(ScannerWrapper.nextLine("New Username: "));
                    }
                    case 4 ->{
                        String value = ScannerWrapper.nextLine("New Password: ");
                        LibraryManger.getInstance().getAdminByPassword().remove(target.getPassword());
                        target.setPassword(value);
                        LibraryManger.getInstance().getAdminByPassword().put(target.getPassword(), target);
                    }
                    case 5 ->{
                        return;
                    }
                    default->{
                        ScannerWrapper.rewritePrompt(ERROR);
                        ScannerWrapper.pause();
                        continue;
                    }
                }
                System.out.println(ConsoleStyle.GREEN + "Admin updated successfully." + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            }
        }catch(Exception e){
            System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
            ScannerWrapper.pause();
        }
    }

    private static void seeAllEntities(){
        while (true) {
            ConsoleStyle.clearScreen();
            Pagination<Entity> pagination = new Pagination<>(LibraryManger.getInstance().getAllEntities());
            System.out.println("--------Page " + pagination.getCurrentPageNumber() + "/" + pagination.gerTotalPageNumber() + "--------");
            for (Entity entity : pagination.getCurrentPage()) {
                System.out.println(ConsoleStyle.YELLOW + entity.toString() + ConsoleStyle.RESET);
            }
            System.out.println(
                    "==============================\n" + ConsoleStyle.BG_WHITE + ConsoleStyle.CYAN +
                            (pagination.hasNextPage() ? "N. Next Page        \n" : "") +
                            (pagination.hasPreviousPage() ? "P. Previous Page    \n" : "") +
                        "------------------------------\n" +
                            "Search By     \n" +
                            "1. First Name \n" +
                            "2. Last Name  \n" +
                            "3. Password   \n" +
                            "4. Back       \n" +
                            "E. Exit               " + ConsoleStyle.RESET);
            String select = ScannerWrapper.nextLine(SELECTION);
            List<Entity> result = new ArrayList<>();
            switch (select.toLowerCase()) {
                case "1" -> {
                    String firstName = ScannerWrapper.nextLine("First Name: ");
                    result = LibraryManger.getInstance().filteredSearch(entity -> entity.getFirstName().toLowerCase().contains(firstName.toLowerCase()));
                }
                case "2" -> {
                    String lastName = ScannerWrapper.nextLine("Last Name: ");
                    result = LibraryManger.getInstance().filteredSearch(entity -> entity.getLastName().toLowerCase().contains(lastName.toLowerCase()));
                }
                case "3" -> {
                    String password = ScannerWrapper.nextLine("Password: ");
                    result = LibraryManger.getInstance().filteredSearch(entity -> entity.getPassword().equals(password));
                }
                case "n" -> pagination.nextPage();
                case "p" -> pagination.previousPage();
                case "e" -> {
                    return;
                }
                default -> {
                    ScannerWrapper.rewritePrompt(ERROR);
                    ScannerWrapper.pause();
                }
            }
            if (result.isEmpty()){
                System.out.println(ConsoleStyle.RED + "No Result Found." + ConsoleStyle.RESET);
            } else {
                for (Entity entity : result) {
                    System.out.println(ConsoleStyle.YELLOW + entity + ConsoleStyle.RESET);
                }
            }
            ScannerWrapper.pause();
        }
    }

    private static void systemSettings(Admin admin){

        while(true){

            ConsoleStyle.clearScreen();

            System.out.println(
                            ConsoleStyle.BLUE +
                            "========== System Properties ==========\n" +
                            "Current Values:\n" +
                            "Borrow Time: " + SystemProperties.getInstance().getBaseBorrowTime() + "\n" +
                            "Reserve Limit: " + SystemProperties.getInstance().getReserveLimit() + "\n" +
                            "Reserve Expire Days: " + SystemProperties.getInstance().getReserveExpireDays() + "\n" +
                            "Base Fine: " + SystemProperties.getInstance().getBaseFineRate() + "\n" +
                            "Daily Fine: " + SystemProperties.getInstance().getDailyFineRate() + "\n\n" +
                            "=======================================\n\n" + ConsoleStyle.BG_WHITE +
                            "1. Borrow Time         \n" +
                            "2. Reserve Limit       \n" +
                            "3. Reserve Expire Days \n" +
                            "4. Base Fine           \n" +
                            "5. Daily Fine          \n" +
                            "6. Back                \n" +
                            ConsoleStyle.RESET);
            int select = ScannerWrapper.nextInt(SELECTION);
            try{
                switch(select){
                    case 1 -> updateBorrowTime(admin);
                    case 2 -> updateReserveLimit(admin);
                    case 3 -> updateReserveExpire(admin);
                    case 4 -> updateBaseFine(admin);
                    case 5 -> updateDailyFine(admin);
                    case 6 ->{
                        return;
                    }
                    default ->{
                        ScannerWrapper.rewritePrompt(ERROR);
                        ScannerWrapper.pause();
                    }
                }
            }catch(Exception e){
                System.out.println(ConsoleStyle.RED + e.getMessage() + ConsoleStyle.RESET);
                ScannerWrapper.pause();
            }
        }
    }

    private static void updateBorrowTime(Admin admin){
        int value = ScannerWrapper.nextInt("Borrow Time (Days): ");
        SystemProperties.getInstance().setBaseBorrowTime(admin,value);
        System.out.println(ConsoleStyle.GREEN + "Updated Successfully." + ConsoleStyle.RESET);
        ScannerWrapper.pause();
    }

    private static void updateReserveLimit(Admin admin){
        int value = ScannerWrapper.nextInt("Reserve Limit: ");
        SystemProperties.getInstance().setReserveLimit(admin,value);
        System.out.println(ConsoleStyle.GREEN + "Updated Successfully." + ConsoleStyle.RESET);
        ScannerWrapper.pause();
    }

    private static void updateReserveExpire(Admin admin){
        int value = ScannerWrapper.nextInt("Reserve Expire Days: ");
        SystemProperties.getInstance().setReserveExpireDays(admin,value);
        System.out.println(ConsoleStyle.GREEN + "Updated Successfully." + ConsoleStyle.RESET);
        ScannerWrapper.pause();
    }

    private static void updateBaseFine(Admin admin){
        double value = ScannerWrapper.nextDouble("Base Fine: ");
        SystemProperties.getInstance().setBaseFineRate(admin,value);
        System.out.println(ConsoleStyle.GREEN + "Updated Successfully." + ConsoleStyle.RESET);
        ScannerWrapper.pause();
    }

    private static void updateDailyFine(Admin admin){
        double value = ScannerWrapper.nextDouble("Daily Fine: ");
        SystemProperties.getInstance().setDailyFineRate(admin,value);
        System.out.println(ConsoleStyle.GREEN + "Updated Successfully." + ConsoleStyle.RESET);
        ScannerWrapper.pause();
    }

}
