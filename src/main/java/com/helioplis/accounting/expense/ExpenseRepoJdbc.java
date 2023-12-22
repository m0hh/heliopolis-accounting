//package com.helioplis.accounting.expense;
//
//import lombok.AllArgsConstructor;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//@AllArgsConstructor
//public class ExpenseRepoJdbc {
//    private final JdbcTemplate jdbcTemplate;
//
//    public List<Expense> findExpensesFilter(){
//        String sql = "SELECT * FROM expenses WHERE (:beforeDate IS NULL OR created_at >= :beforeDate) AND (:afterDate IS NULL OR created_at <= :afterDate)";
//        List<Expense> expenses = jdbcTemplate.query(sql,)
//    }
//
//    private static RowMapper<Expense> mapExpenseFromDb(){
//        return (rs,rowNumber) ->{
//            Integer id = rs.getInt("id");
//
//        }
//    }
//}
