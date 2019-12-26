package com.sachindroid.app.config;

import com.sachindroid.app.model.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.sachindroid.app.tasks.TaskOne;
import com.sachindroid.app.tasks.TaskTwo;
import org.springframework.core.io.Resource;

@Configuration
public class BatchConfig {
    @Bean
    public Job job(JobBuilderFactory jbf, StepBuilderFactory sbf, ItemReader<Employee> reader, ItemProcessor<Employee,Employee> processor, ItemWriter<Employee> writer){
        //It will process in batches of 100
        Step step=sbf.get("etl-file-load").<Employee,Employee>chunk(100).reader(reader).processor(processor).writer(writer).build();
        return jbf.get("etl-load").incrementer(new RunIdIncrementer()).start(step).build();
    }

    @Bean
    public FlatFileItemReader<Employee> flatFileItemReader(@Value("${input}") Resource resource){
        FlatFileItemReader<Employee> ffir=new FlatFileItemReader<>();
        ffir.setResource(resource);
        ffir.setName("CSV Reader");
        ffir.setLinesToSkip(1);
        ffir.setLineMapper(lineMapper());
        return ffir;
    }


    private LineMapper<Employee> lineMapper(){
        DefaultLineMapper<Employee> defaultLineMapper=new DefaultLineMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer=new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setNames(new String[]{"id","firstName","lastName","email","age"});
        delimitedLineTokenizer.setStrict(false);
        BeanWrapperFieldSetMapper<Employee> fieldSetMapper=new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Employee.class);
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        return defaultLineMapper;
    }
//    @Autowired
//    private JobBuilderFactory jobBuilderFactory;
//    @Autowired
//    private StepBuilderFactory stepBuilderFactory;
//    @Bean
//    public Step stepOne(){
//        return  stepBuilderFactory.get("stepOne").tasklet(new TaskOne()).build();
//    }
//    @Bean
//    public Step stepTwo(){
//        return stepBuilderFactory.get("stepTwo").tasklet(new TaskTwo()).build();
//    }
//    @Bean
//    public Job demoJob(){
//        return jobBuilderFactory.get("demoJob").incrementer(new RunIdIncrementer()).start(stepOne()).next(stepTwo()).build();
//    }
}
