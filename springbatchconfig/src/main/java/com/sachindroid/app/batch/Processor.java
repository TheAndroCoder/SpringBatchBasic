package com.sachindroid.app.batch;

import com.sachindroid.app.model.Employee;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Processor implements ItemProcessor<Employee,Employee> {
    private static final Map<String,String> map=new HashMap<>();

    public Processor(){
        map.put("E101","1");
        map.put("E102","2");
    }
    @Override
    public Employee process(Employee employee) throws Exception {
        if(map.containsKey(employee.getId()))
        employee.setId(map.get(employee.getId()));
        return employee;
    }
}
