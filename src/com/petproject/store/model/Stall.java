package com.petproject.store.model;

import com.petproject.store.services.StorePerformanceService;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Stall {

    Logger log;
    List<Seller> sellers;
    AtomicInteger servedBuyers = new AtomicInteger(0);
    StorePerformanceService performanceService = new StorePerformanceService();

    public Stall(Logger log, List<Seller> sellers) {
        this.log = log;
        this.sellers = sellers;
    }



    public void trade(Queue<Buyer> buyers) {


        servedBuyers.set(0);
        performanceService.startServeBuyers();


//        sellers.parallelStream().forEach(seller -> {
//            seller.serveTheBuyer(buyers.poll());
//            servedBuyers.incrementAndGet();
//        });
//        for (int i = 0; i < sellers.size(); i++) {
//
//        }



               for (int i = 0; i < sellers.size(); i++) {
                   Seller seller = sellers.get(i);

                   new Thread(()-> {
                       try {
                           seller.serveTheBuyer(buyers.poll());
                       }
                       catch (java.lang.NullPointerException e){
                           try {
                               Thread.sleep(1000);
                           } catch (InterruptedException q) {
                               q.printStackTrace();
                           }
                       }
                       servedBuyers.incrementAndGet();
                   }).start();
               }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info(performanceService.checkPerformance(servedBuyers.get()));



    }

}
