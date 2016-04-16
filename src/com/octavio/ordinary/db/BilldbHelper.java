package com.octavio.ordinary.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Provides access to a database of notes. Each note has a title, the note
 * itself, a creation date and a modified data.
 */
public class BilldbHelper {

    private static final String TAG = "Cola_BilldbHelper";

//    private static final String DATABASE_NAME = "cola.db";
    private static final String DATABASE_NAME = "record.db";
    
    SQLiteDatabase db;
    Context context;
    
    public BilldbHelper(Context _context) {
    	context=_context;
    	db=context.openOrCreateDatabase(DATABASE_NAME, 0, null); 
    	Log.v(TAG,"db path="+db.getPath());
    }
    
    public void CreateTable_acctitem() {
    	try{
	        db.execSQL("CREATE TABLE acctitem ("
	                + "_ID INTEGER PRIMARY KEY,"
	                + "PID integer,"
	                + "NAME TEXT"               
	                + ");");
	        Log.v("cola","Create Table acctitem ok");
        }catch(Exception e){
        	Log.v("cola","Create Table acctitem err,table exists.");
        }
    }
    
    public void CreateTable_bills() {
    	try{
	        db.execSQL("CREATE TABLE bills ("
	                + "_id INTEGER primary key autoincrement,"
	                +" acctitemid integer,"   
	                + "fee real,"
	                + "userid integer,"
	                + "sdate TEXT,"
	                + "stime TEXT,"
	                + "desc TEXT"                
	                + ");");
	        
	        Log.v("cola","Create Table acctitem ok");
        }catch(Exception e){
        	Log.v("cola","Create Table acctitem err,table exists.");
        }
    }
    
    public boolean Bills_save(int acctid,int fee,int userid,String date,String time,String text){
    	String sql="";
    	try{
    		sql="insert into bills values(null,"+acctid+","+fee+","+userid+",'"+date+"','"+time+"','"+text+"')";
	        db.execSQL(sql);
	        
	        Log.v("cola","insert Table bills ok");
	        return true;
	        
        }catch(Exception e){
        	Log.v("cola","insert Table bills err="+sql);
        	return false;
        }
    }
    
    public void CreateTable_colaconfig() {
    	try{
	        db.execSQL("CREATE TABLE colaconfig ("
	                + "_ID INTEGER PRIMARY KEY,"
	                + "NAME TEXT"            
	                + ");");
	        Log.v("cola","Create Table colaconfig ok");
	    }catch(Exception e){
	    	Log.v("cola","Create Table acctitem err,table exists.");
	    }
    }
    
    public void CreateTable_users() {
    	try{
	        db.execSQL("Create table tusers (_id integer primary key autoincrement," +
					"caption text not null)");
	        Log.v("cola","Create Table users ok");
	        db.execSQL("insert into tusers values (null,'个人')");
	        db.execSQL("insert into tusers values (null,'组织')");
	        db.execSQL("insert into tusers values (null,'家庭')");
	    }catch(Exception e){
	    	Log.v("cola","Create Table tusers err,table exists.");
	    }
    }
    
    public void InitAcctitem() {
    	try{
    	  //s.getBytes(encoding);
    	  db.execSQL("insert into acctitem values (1,null,'收入')");
          db.execSQL("insert into acctitem values (2,1,'工资')");
          db.execSQL("insert into acctitem values (3,1,'中奖')");
          db.execSQL("insert into acctitem values (4,1,'自主营业')");
          db.execSQL("insert into acctitem values (5,1,'金融投资')");
          db.execSQL("insert into acctitem values (6,1,'股票分红')");
          db.execSQL("insert into acctitem values (9998,1,'其他收入')");
          
          db.execSQL("insert into acctitem values (0,null,'支出')");
          db.execSQL("insert into acctitem values (7,0,'生活用品')");
          db.execSQL("insert into acctitem values (8,0,'外出就餐')");
          db.execSQL("insert into acctitem values (9,0,'服装配饰')");
          db.execSQL("insert into acctitem values (10,0,'交通通讯')");
          db.execSQL("insert into acctitem values (11,0,'游玩娱乐')");
          db.execSQL("insert into acctitem values (12,0,'水电物业')");
          db.execSQL("insert into acctitem values (13,0,'金融保险')");
          db.execSQL("insert into acctitem values (14,0,'医疗保健')");
          db.execSQL("insert into acctitem values (15,0,'人际交往')");
          db.execSQL("insert into acctitem values (9999,0,'其他支出')");
          
          //db.execSQL("insert into bills values(100,135,10000,'','','备注')");
          Log.v("cola","insert into ok"); 
    	}catch(Exception e)
    	{
    		Log.v("cola","init acctitem e="+e.getMessage());
    	}
        
    }
    public void Acctitem_newitem(String text,int type){
    	
    	Cursor c =db.query("acctitem", new String[]{"max(_id)+1"}, "_id is not null and _id<9998", null, null, null, null);
    	c.moveToFirst();
    	int maxid=c.getInt(0);    	
    	String sql="insert into acctitem values ("+maxid+","+type+",'"+text+"')";
    	db.execSQL(sql);
    	Log.v("cola","newitem ok text="+text+" id="+type+" sql="+sql);
    	
    }
    
    public void Acctitem_edititem(String text,int id){    	
    	db.execSQL("update acctitem set name='"+text+"' where _id="+id);
    	Log.v("cola","edititem ok text="+text+" id="+id);
    }
    
    public void Acctitem_delitem(int id){
    	
    	db.execSQL("delete from acctitem where _id="+id);
    	Log.v("cola","delitem ok id="+id);
    }
    
    public void QueryTable_acctitem(){
    	
    }
    
    public void FirstStart(){
    	try{
	    	String col[] = {"type", "name" };
	    	Cursor c =db.query("sqlite_master", col, "name='colaconfig'", null, null, null, null);
	    	int n=c.getCount();
	    	if (c.getCount()==0){
	    		CreateTable_acctitem();
	    		CreateTable_colaconfig();
	    		CreateTable_bills();
	    		CreateTable_users();
	    		InitAcctitem();    		
	    	}	    	
	    	//test();	    	
	    	Log.v("cola","c.getCount="+n+"");
	    	    	
	    	
    	}catch(Exception e){
    		Log.v("cola","e="+e.getMessage());
    	}
    	
    	
    }
    
    
    public void close(){
    	db.close();
    }
    
    public Cursor getParentNode(){
    	return db.query("acctitem", new String[]{"_id", "name","pid" }, "pid is null", null, null, null, "pid,_id");    	
  
    }
    
    public Cursor getChildenNode(String pid){
    	Log.v("cola","run getchildenNode");
   		return db.query("acctitem", new String[]{"_id", "name" }, "pid="+pid, null, null, null, "_id");    	

    }
   
    public Cursor getUserid(){
    	Log.v("cola","run get users cursor");
   		return db.query("tusers", new String[]{"_id", "caption" }, null, null, null, null, null);    	

    }
    
    public Cursor getBills(String date){
    	Log.v("cola","run get bills cursor date="+date);
   		return db.query("bills a,acctitem b,tusers c", new String[]{"a._id _id","a.rowid rowid", "acctitemid","b.name||'      '||c.caption name","b._id bid","( case when pid=0 then '-' else '' end)||fee/100||'' fee","sdate||' '||stime sdate","desc" }, "a.userid=c._id and a.acctitemid=b._id and a.sdate like '"+date+"%'", null, null, null, null);    	

    }
    
    public void delBills(int id)
    {
    	db.execSQL("delete from bills where _id="+id);
    }
    
    public String getBillsTotal(String date){
    	Log.v("cola","run get bills total cursor");
   		Cursor cur=db.query("bills a,acctitem b", new String[]{"sum(case when b.pid=0 then -fee end)/100||'' out","sum(case when b.pid=1 then fee end)/100||'' infee","sum(case when b.pid=0 then -fee else fee end)/100||'' total"}, "a.acctitemid=b._id and a.sdate like '"+date+"%'",null, null, null, null);    	
   		cur.moveToFirst(); 
   		String s="";
	    while(!cur.isAfterLast()){
	    	
	    	s="收入:"+cur.getFloat(1)+" 支出:"+cur.getFloat(0)+" 小计:"+cur.getFloat(2);
	    	cur.moveToNext(); 
	    }
	    return s;
    }
    
    public String test(){
    	try{    		    	   
    		Cursor c2 =getUserid();
    		String ss="";  
    		c2.moveToFirst(); 
    	    while(!c2.isAfterLast()){
    	    	
    	    	ss = c2.getString(0) +", "+ c2.getString(1);
    	    	//byte b[]=c2.getString(1).getBytes();
    	    	
    	    	c2.moveToNext(); 
    	    	
    	    	Log.v("cola","ss="+ss+"");
    		}
    	    	
    	    return ss;
    	}catch(Exception e){
    		Log.v("cola","e="+e.getMessage());
    		return "err";
    	}
    }


}

