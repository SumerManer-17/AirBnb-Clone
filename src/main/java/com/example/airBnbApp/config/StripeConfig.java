package com.example.airBnbApp.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class StripeConfig {
    public StripeConfig(@Value("${stripe.secrete.key}")String stripesecretekey){
        Stripe.apiKey=stripesecretekey;
    }
}
