package com.mycompany.newbie.cucumber;

import com.mycompany.newbie.NewbieApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = NewbieApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
