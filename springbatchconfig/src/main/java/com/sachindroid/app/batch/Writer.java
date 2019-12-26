package com.sachindroid.app.batch;

import com.sachindroid.app.model.Employee;
import com.sachindroid.app.repository.EmployeeRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Writer implements ItemWriter<Employee> {

    @Autowired
    private EmployeeRepository repository;
    @Override
    public void write(List<? extends Employee> list) throws Exception {
        System.out.println("data saved for user : "+list.get(0).getEmail());
        repository.saveAll(list);
    }
}
