package com.finartz.homework.TicketService.util;

import java.util.Random;
import java.util.UUID;

public class StringGenerator {
    public static String generatePnr(String ticketId){
        String pnr;

        int digit = 5 - ticketId.length();
        StringBuilder ticketIdBuilder = new StringBuilder(ticketId);
        for (int i = 0; i < digit ; i++){
            Random rnd = new Random();
            char c = (char) (rnd.nextInt(26) + 'a');

            ticketIdBuilder.insert(0, c);
        }
        ticketId = ticketIdBuilder.toString();

        String generatedString = UUID.randomUUID().toString();
        generatedString = generatedString.replace("-","").substring(0,10);

        pnr = ticketId + generatedString;
        return pnr;
    }
}
