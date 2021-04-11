package com.foxconn.webservice;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class DBUtil {
    private ArrayList<String> arrayList = new ArrayList<String>();
    private ArrayList<String> brrayList = new ArrayList<String>();
    private ArrayList<String> crrayList = new ArrayList<String>();

    private HttpConnSoap Soap = new HttpConnSoap();

    public static Connection getConnection() {
        Connection con = null;
        try {
            //Class.forName("org.gjt.mm.mysql.Driver");
            //con=DriverManager.getConnection("jdbc:mysql://192.168.0.106:3306/test111?useUnicode=true&characterEncoding=UTF-8","root","initial");
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return con;
    }

    public class ThirdThread implements Callable<ArrayList<String>>{
        public ArrayList<String> call(){
            crrayList = Soap.GetWebServre ("selectAllCargoInfor", arrayList, brrayList);
            return crrayList;
        }
    }

    /**
     * ?????????
     *
     * @return
     */
    public List<HashMap<String, String>> getAllInfo() {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        arrayList.clear();
        brrayList.clear();
        crrayList.clear();


        ThirdThread rt = new ThirdThread(); //??Callable??
        FutureTask<ArrayList<String>> task = new FutureTask<ArrayList<String>>(rt);//??FutrueTask???Callable??
        new Thread(task, "???????").start(); //??????Callable?????????
        try {
            crrayList = task.get();
        } catch (InterruptedException e) {
            // TODO ????? catch ?
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO ????? catch ?
            e.printStackTrace();
        }

//		new Thread() {
//			public void run()
//			{
//				try{
//					crrayList = Soap.GetWebServre ("selectAllCargoInfor", arrayList, brrayList);
//				}
//				catch(Exception e) {
//
//				}
//			}
//		}.start(); 	//???Callable?Future


        //??ListView????Key,Value?
        HashMap<String, String> tempHash = new HashMap<String, String>();
        tempHash.put("Cno", "Cno");
        tempHash.put("Cname", "Cname");
        tempHash.put("Cnum", "Cnum");
        tempHash.put("MO", "MO");
        tempHash.put("PN", "PN");
        list.add(tempHash);

        if(crrayList!=null && !crrayList.isEmpty())

            for (int j = 0; j < crrayList.size(); j += 5) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("Cno", crrayList.get(j));
                hashMap.put("Cname", crrayList.get(j + 1));
                hashMap.put("Cnum", crrayList.get(j + 2));
                hashMap.put("MO", crrayList.get(j + 3));
                hashMap.put("PN", crrayList.get(j + 4));
                list.add(hashMap);
            }

        return list;
    }

    /**
     * ????????
     *
     * @return
     */
    public void insertCargoInfo(String Cname, String Cnum) {

        arrayList.clear();
        brrayList.clear();

        arrayList.add("Cname");
        arrayList.add("Cnum");
        brrayList.add(Cname);
        brrayList.add(Cnum);

        new Thread(){
            public void run()
            {
                try{
                    Soap.GetWebServre("insertCargoInfo", arrayList, brrayList);
                }
                catch(Exception e) {
                }
            }
        }.start();
        //Soap.GetWebServre("insertCargoInfo", arrayList, brrayList);
    }

    /**
     * ????????
     *
     * @return
     */
    public void deleteCargoInfo(String Cno) {

        arrayList.clear();
        brrayList.clear();

        arrayList.add("Cno");
        brrayList.add(Cno);

        new Thread(){
            public void run()
            {
                try{
                    Soap.GetWebServre("deleteCargoInfo", arrayList, brrayList);
                }
                catch(Exception e) {
                }
            }
        }.start();

    }

    /**
     * ??????
     *
     * @return
     */
    public List<HashMap<String, String>> Checkemp(String Emp,String station) {

        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        arrayList.clear();
        brrayList.clear();
        crrayList.clear();

        arrayList.add("stremp");
        arrayList.add("station");
        brrayList.add(Emp);
        brrayList.add(station);

        new Thread(){
            public void run()
            {
                try{
                    crrayList = Soap.GetWebServre("Checkemp", arrayList, brrayList);
                }
                catch(Exception e) {
                }
            }
        }.start();

        try {
            //????????????????????
            Thread.sleep(200);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        HashMap<String, String> tempHash = new HashMap<String, String>();
        tempHash.put("Emp", "Emp");

        list.add(tempHash);

        if(crrayList!=null && !crrayList.isEmpty()){

            for (int j = 0; j < crrayList.size(); j += 1) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("Emp", crrayList.get(j));

                list.add(hashMap);
            }
        }
        return list;
    }

    /**
     * ??SN?????????P/N?ErrorCode
     *
     * @return
     */
    public List<HashMap<String, String>> VIErrorCode (String SN) {

        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        arrayList.clear();
        brrayList.clear();
        crrayList.clear();

        arrayList.add("strsn");
        brrayList.add(SN);

        new Thread(){
            public void run()
            {
                try{
                    crrayList = Soap.GetWebServre("VIErrorCode", arrayList, brrayList);
                }
                catch(Exception e) {
                }
            }
        }.start();

        try {
            //????????????????????
            Thread.sleep(500);

        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        HashMap<String, String> tempHash = new HashMap<String, String>();
        tempHash.put("SN", "SN");
        tempHash.put("MO", "MO");
        tempHash.put("PN", "PN");
        tempHash.put("CODE1", "CODE1");
        tempHash.put("CODE2", "CODE2");
        list.add(tempHash);
        //?Bug
        if(crrayList!=null && !crrayList.isEmpty() &&crrayList.size() == 1){

            for (int j = 0; j < crrayList.size(); j += 1) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("ERROR", crrayList.get(j));

                list.add(hashMap);
            }
        }
        if(crrayList!=null && !crrayList.isEmpty() &&crrayList.size() >= 5){

            for (int j = 0; j < crrayList.size(); j += 5) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("SN", crrayList.get(j));
                hashMap.put("MO", crrayList.get(j+1));
                hashMap.put("PN", crrayList.get(j+2));
                hashMap.put("CODE1", crrayList.get(j+3));
                hashMap.put("CODE2", crrayList.get(j+4));

                list.add(hashMap);
            }
        }
        return list;
    }

    /**
     * ???????????????
     *
     * @return
     */
    public List<HashMap<String, String>> VIReNum(String data) {

        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        arrayList.clear();
        brrayList.clear();
        crrayList.clear();

        arrayList.add("str");
        brrayList.add(data);

        new Thread(){
            public void run()
            {
                try{
                    crrayList = Soap.GetWebServre("VIReNum", arrayList, brrayList);
                }
                catch(Exception e) {
                }
            }
        }.start();

        try {
            //????????????????????
            Thread.sleep(500);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        HashMap<String, String> tempHash = new HashMap<String, String>();
        tempHash.put("SN", "SN");
        tempHash.put("MO", "MO");
        tempHash.put("CODE1", "CODE1");
        tempHash.put("CODE2", "CODE2");
        tempHash.put("COUNT", "COUNT");

        list.add(tempHash);
        //?Bug
        if(crrayList!=null && !crrayList.isEmpty() &&crrayList.size() >= 5){

            for (int j = 0; j < crrayList.size(); j += 5) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("SN", crrayList.get(j));
                hashMap.put("MO", crrayList.get(j+1));
                hashMap.put("CODE1", crrayList.get(j+2));
                hashMap.put("CODE2", crrayList.get(j+3));
                hashMap.put("COUNT", crrayList.get(j+4));

                list.add(hashMap);
            }
        }

        return list;
    }

    /**
     * PASS/FAIL??
     *
     * @return
     */
    public List<HashMap<String, String>> VIOverStation (String data) {

        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        arrayList.clear();
        brrayList.clear();
        crrayList.clear();

        //?????
        arrayList.add("strate");
        brrayList.add(data);

        new Thread(){
            public void run()
            {
                try{
                    crrayList = Soap.GetWebServre("VIOverStation", arrayList, brrayList);
                }
                catch(Exception e) {
                }
            }
        }.start();

        try {
            //????????????????????
            Thread.sleep(350);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        HashMap<String, String> tempHash = new HashMap<String, String>();
        tempHash.put("DATA", "DATA");
        //tempHash.put("MO", "MO");
        //tempHash.put("CODE1", "CODE1");
        //tempHash.put("CODE2", "CODE2");
        //tempHash.put("COUNT", "COUNT");

        list.add(tempHash);
        //?Bug
        if(crrayList!=null && !crrayList.isEmpty() && crrayList.size() == 1){

            for (int j = 0; j < crrayList.size(); j += 1) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("DATA", crrayList.get(j));
                //hashMap.put("MO", crrayList.get(j+1));
                //hashMap.put("CODE1", crrayList.get(j+2));
                //hashMap.put("CODE2", crrayList.get(j+3));
                //hashMap.put("COUNT", crrayList.get(j+4));

                list.add(hashMap);
            }
        }

        return list;
    }
}
