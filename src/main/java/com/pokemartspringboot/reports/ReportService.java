package com.pokemartspringboot.reports;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    String path = "C:\\Users\\Marwhin Dean\\Documents\\Programming\\Practice\\backend\\poke-mart-spring-boot\\src\\main\\resources";

    public void exportReport() throws FileNotFoundException, JRException {
        List<UserReport> userReports = populateUserReports();

        File file = ResourceUtils.getFile("classpath:pokemart-user-report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userReports);
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("createdBy", "mdean");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint,  path + "\\pokemart-user-reports.pdf");
    }

    private List<UserReport> populateUserReports() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT u.user_name, p.product_name, SUM(ci.quantity) AS \"total_quantity\", SUM((ci.quantity * p.product_price)) AS \"total_amount\"\n" +
                "\tFROM users u JOIN carts c ON u.user_id = c.user_id\n" +
                "\tJOIN cart_items ci ON ci.cart_id = c.cart_id\n" +
                "\tJOIN products p ON p.product_id = ci.product_id\n" +
                "WHERE c.checked_out = true\n" +
                "GROUP BY u.user_name, p.product_name\n" +
                "ORDER BY u.user_name, \"total_amount\";");
        return jdbcTemplate.query(query.toString(), new BeanPropertyRowMapper<UserReport>(UserReport.class));
    }
}
