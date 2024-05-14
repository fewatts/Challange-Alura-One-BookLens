package com.API.BookLens.main;

import com.API.BookLens.service.GetBookInfos;

public class Main {

    public void menu() {
       System.out.println(GetBookInfos.getData("http://gutendex.com/books/?ids=11,12,13"));
    }
    
}