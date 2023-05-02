package com.square.examples;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import com.squareup.square.*;
import com.squareup.square.api.*;
import com.squareup.square.models.*;

public class Quickstart {
    public static void main(String[] args) {

        InputStream inputStream =
            Quickstart.class.getResourceAsStream("/config.properties");
        Properties prop = new Properties();

        try {
            prop.load(inputStream);
        } catch (IOException e) {
            System.out.println("Error reading properties file");
            e.printStackTrace();
        }

        SquareClient client = new SquareClient.Builder()
            .accessToken(prop.getProperty("SQUARE_ACCESS_TOKEN"))
            .environment(Environment.SANDBOX)
            .build();

        CheckoutApi checkoutApi = client.getCheckoutApi();

        List<CatalogItem> catalogItems = new ArrayList<>();
        CatalogApi api = client.getCatalogApi();
        try{
            ListCatalogResponse resp = api.listCatalog(null, "ITEM", null);
            for (CatalogObject obj : resp.getObjects()) {
                CatalogItem item = obj.getItemData();
                catalogItems.add(item);
            }
        } catch (Exception ex) {
            System.out.println("failed to get stuff");
        }

        System.out.println(catalogItems.get(0).getName());

        CreatePaymentLinkRequest body = new CreatePaymentLinkRequest.Builder()
                .checkoutOptions(new CheckoutOptions.Builder()
                        .acceptedPaymentMethods(new AcceptedPaymentMethods.Builder().applePay(true).googlePay(true).build()).build())
                .prePopulatedData(new PrePopulatedData.Builder().buyerEmail("vasily@pupkin.com")
                        .buyerAddress(new Address.Builder().country("CA").firstName("Vasily").lastName("Pupkin").postalCode("V9Y L6T")
                                .locality("Somewhere").addressLine1("4678 Some Street.").build()).build())
                .quickPay(new QuickPay.Builder(
                        catalogItems.get(0).getName(),
                        new Money.Builder()
                        .amount(100L)
                        .currency("CAD")
                        .build(),
                        "<LOCATITON_ID>"
                ).build()).build();

        try {
            CreatePaymentLinkResponse presp = checkoutApi.createPaymentLink(body);
            presp.getErrors();
            System.out.println(presp);
        } catch (Exception ex) {
            ex.getMessage();
            System.out.println(ex);
        }

        SquareClient.shutdown();
    }
}
