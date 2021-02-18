package ru.litvinov;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.litvinov.config.Conf;
import ru.litvinov.models.Auto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Component
public class Main {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static void main(final String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Conf.class);

        Main main = context.getBean("main",Main.class);
        List<Auto> results = main.jdbcTemplate.query(
                "select * from auto",
                new RowMapper<Auto>() {
                    public Auto mapRow(ResultSet resultSet, int i) throws SQLException {
                        Auto auto = new Auto(resultSet.getString("model")
                                ,resultSet.getInt("maxspeed")
                                ,resultSet.getInt("mileage"));
                        auto.setId(resultSet.getLong("id"));
                        return auto;
                    }
                });
                main.jdbcTemplate.update("CREATE SEQUENCE IF NOT EXISTS  auto_seq START WITH 99999 increment by 1 ");
                Auto[] autos = {new Auto("1",1,1),new Auto("2",2,2), new Auto("3",3,3)};
                main.batch(main.jdbcTemplate,autos);
    }

    public void batch(JdbcTemplate jdbcTemplate , final Auto... autos){


        int[] ints = jdbcTemplate.batchUpdate("INSERT into AUTO VALUES (default,?, ?, ?)", new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {

                System.out.println(i);
                preparedStatement.setString(1,autos[i].getModel());
                preparedStatement.setInt(2,autos[i].getMaxspeed());
                preparedStatement.setInt(3,autos[i].getMileage());
            }

            public int getBatchSize() {
                return autos.length;
            }
        });

        System.out.println(Arrays.stream(ints).sum());
    }
}
