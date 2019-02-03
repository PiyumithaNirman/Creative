package com.example.piyumitha.good;

public class Constants {
    //public static final String LOGIN_URL = "http://192.168.43.234/user/login%20and%20registration/";
    public static final String HOST = "http://192.168.43.143:5000/";
    public static final String LOGIN_URL = HOST + "login";
    public static final String REGISTER_URL = HOST + "register";

    // admin
    public static final String ADMIN_DETAILS_URL = HOST + "company/profile";
    public static final String ADMIN_ADD_ROLE_URL = HOST + "company/AddRole";
    public static final String ADMIN_ADD_EMPLOYEE_URL = HOST + "company/addEmployee";

    // manager
    public static final String MANAGER_ADD_PROJECT = HOST + "manager/newProjects/addNew";
    public static final String MANAGER_ALL_PROJECT = HOST + "manager/allProjects";

    public static final String MANAGER_ADD_TRANSACTION = HOST + "manager/projects/addTransaction";
    public static final String MANAGER_VIEW_FINANCE = HOST + "manager/projects/viewFinance";

    // employee
    public static final String EMPLOYEE_LEAVES = HOST + "employee/leaves";
    public static final String EMPLOYEE_REQUEST_LEAVE = HOST + "employee/leaveRequest";
    public static final String EMPLOYEE__LEAVE_HISTORY = HOST + "employee/leaveHistory";
    public static final String EMPLOYEE__SALARY_HISTORY = HOST + "employee/salaryHistory";


}