package com.foxconn.main;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bgxt.gallerydemo.R;
import com.foxconn.webservice.*;
import com.foxconn.http.*;
import com.foxconn.ichart.A2Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {

    public static HashMap<String, Bitmap> imagesCache = new HashMap<String, Bitmap>(); // 芞遣湔
    public static ImageAdapter imageAdapter;
    private int num = 0;
    List<String> urls = new ArrayList<String>(); // 垀衄芞華硊List
    List<String> url = new ArrayList<String>(); // 剒猁狟婥芞腔url華硊
    List<HashMap<String, String>> datalist = new ArrayList<HashMap<String, String>>();//黍webservice鳳腕腔杅擂蹈桶

    public Gallery gallery;

    public TextView XYPointer;
    public TextView currentTime;
    public EditText passwd;
    public EditText sn;
    public EditText serverIP;
    public TextView pn;
    public TextView mo;

    public TextView passQty;
    public TextView failQty;
    public TextView failRate;
    public TextView position;
    public TextView connection;
    public TextView txtCode;
    public TextView txtFace;

    public ImageView image;
    public ImageView failBtn;
    public ImageView passBtn;
    public ImageView searchBtn;
    public ImageView settingBtn;

    private Spinner group = null;
    public ListView listView;
    public SimpleAdapter adapter;
    public DBUtil dbUtil;

    public boolean havePermisson = false;//埜馱岆瘁衄癹
    public String errorCode1 = null;//莉腔祥謎測鎢
    public String errorCode2 = null;//莉腔祥謎測鎢
    public String passData = null;
    public String failData = null;//莉祥謎奀跤SFC楷冞腔杅擂
    public String webserviceUrl = null;
    public String productFace = null;//莉腔醱梗
    public String coordinateValue = null;//釴梓硉
    public String groupValue = null;//鳳VI Group硉
    public String emp = null;
    public String strMsg = null;
    public int flag1 = 0;
    public int flag2 = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        String date = sDateFormat.format(new java.util.Date());

        //�例��餞創衾View腔砓
        final DrawView drawView = new DrawView(this);
        //扢离赻隅砱郪璃腔郔湮遵僅睿詢僅
        drawView.setMinimumWidth(0);
        drawView.setMinimumHeight(0);

        //陔膘芞恅璃狟婥繚
        Files.mkdir(this);
        //汒隴覃蚚Webservice 勤砓
        dbUtil = new DBUtil();
        initUI();

        FrameLayout view = (FrameLayout) findViewById(R.id.root);
        currentTime.setText(date);
        groupValue = "VI";//扢离馱桴腔蘇硉峈VI馱桴
        //webserviceUrl = "http://10.120.149.154/VIDisplay/WebServiceVI.asmx";
        //VM
        webserviceUrl = "http://10.224.134.93:8080/ProductivityDisplay/WebServiceVI.asmx";
        //HWT
        //webserviceUrl = "http://10.120.176.142/ProductivityDisplay/WebServiceVI.asmx";

        //忑珂樓徭掛華腔蘇庲
        Bitmap btimap = BitmapFactory.decodeResource(getResources(),
                R.drawable.default_movie_post);

        imagesCache.put("background_non_load", btimap); // 扢离遣湔笢蘇腔芞
        gallery = (Gallery) findViewById(R.id.Gallery1);
        urls.add("");
        urls.add("");
        urls.add("");
        urls.add("");
        urls.add("");
        urls.add("");
        imageAdapter = new ImageAdapter(urls, this);
        gallery.setAdapter(imageAdapter);


        //刲坰偌聽
        searchBtn.setOnClickListener(new SearchBtnListener());
        passBtn.setOnClickListener(new passBtnListener());
        failBtn.setOnClickListener(new failBtnListener());
        settingBtn.setOnClickListener(new settingBtnListener());
        listView.setOnItemClickListener(new OnItemClickListenerImpl());
        group.setOnItemSelectedListener(new OnGroupSelectedListener());

        passwd.setOnKeyListener(this::onEmployeeCommit);

        sn.setOnKeyListener(new checkSNListenerImpl());
        /**
         * class desc: 禸鏡埜馱馱瘍癹
         * <p>Copyright: Copyright(c) 2016 </p>
         * @Version 1.0
         * @Author Ethan
         */
		/*emp.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable text) {

				String strEMP = emp.getText().toString().trim();
				if(strEMP.contains("g7035598")){

					if(strEMP.length() == 0){

						Toast.makeText(MainActivity.this,
								"EMP祥夔峈諾!", Toast.LENGTH_SHORT).show();
						return;
					}
					if(strEMP.contains("\n")){

						//忑珂裁隙陬遙俴賦帣睫
						Pattern CRLF = Pattern.compile("(\n)");
				        Matcher m = CRLF.matcher(strEMP);
				        if (m.find()) {
				        	strEMP = m.replaceAll("");
				        }
					}

					AsyncTaskEmpPermission empPermission = new AsyncTaskEmpPermission();
			        empPermission.execute("G7035598");

			        Toast.makeText(MainActivity.this,
						"Hello", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
		});*/
        /**
         * class desc: 禸鏡莉SN
         * <p>Copyright: Copyright(c) 2016 </p>
         * @Version 1.0
         * @Author Ethan
         */
		/*sn.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable text) {

				String strSN = text.toString().trim();
				if(strSN.contains("g7035598")){
					if(text.toString().length() == 0){
						Toast.makeText(MainActivity.this,
								"SN峈諾!", Toast.LENGTH_SHORT).show();
						return;
					}

					//瓚剿釬珛埜岆瘁衄癹
					if(!havePermisson){
						new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.btn_star)
						.setTitle("恲黹枑尨").setMessage("蠟羶衄釬珛癹ㄛ!")
						.setNegativeButton("OK",new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();// show竭壽瑩

						return;
					}

					if(strSN.contains("\n")){

						//忑珂裁隙陬遙俴賦帣睫
						Pattern CRLF = Pattern.compile("(\n)");
				        Matcher m = CRLF.matcher(strSN);
				        if (m.find()) {
				        	strSN = m.replaceAll("");
				        }
					}
					//雄盄最狟婥莉芞ㄛ樓婥ErrorCode
			        AsyncTaskUrlIsReach urlIsReach= new AsyncTaskUrlIsReach();
					urlIsReach.execute("KMO3315");
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});*/

        /**
         * class desc: 萸僻鳳祥謎弇离釴梓硉
         * <p>Copyright: Copyright(c) 2016 </p>
         * @Version 1.0
         * @Author Ethan
         */
		/*image.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {

				if(image.getDrawable() == null)
					return false;
				if(event.getAction()==MotionEvent.ACTION_DOWN){

					Log.e("point",event.getX() +","+event.getY());

					//載嗣壽衾釴梓蛌遙腔統蕉
					Drawable drawable = image.getDrawable();
					Rect imageBounds = drawable.getBounds();

					Matrix matrix = image.getImageMatrix();
					Rect rect = image.getDrawable().getBounds();
					float[] values = new float[9];
					matrix.getValues(values);

					//場宎趙bitmap腔遵詢
					int intrinsicHeight = drawable.getIntrinsicHeight();
					int intrinsicWidth = drawable.getIntrinsicWidth();
					//褫獗image腔遵詢
					int scaledHeight = imageBounds.height();
					int scaledWidth = imageBounds.width();
					//妏蚚fitXY
					float heightRatio = intrinsicHeight / scaledHeight;
					float widthRatio = intrinsicWidth / scaledWidth;
					//鳳芞砉晚賜硉
					float scaledImageOffsetX = event.getX() - imageBounds.left;
					float scaledImageOffsetY = event.getY() - imageBounds.top;
					//跦擂斕芞砉腔坫溫掀瞰扢离
					float originalImageOffsetX = scaledImageOffsetX * widthRatio;
					float originalImageOffsetY = scaledImageOffsetY * heightRatio;

					drawView.currentX=event.getRawX();
	                drawView.currentY=event.getRawY();
	                //笭餅
	                //drawView.invalidate();
	                drawView.postInvalidate();
	                //鳳X,Y釴梓
	                float tempX = event.getX()/intrinsicWidth;
	                float tempY = event.getY()/intrinsicHeight;

	                DecimalFormat decimalFormat=new DecimalFormat("0.00");
	                String X = decimalFormat.format(tempX);
	                String Y = decimalFormat.format(tempY);

	                coordinateValue = X + "," +Y;

	                Log.d("Coordinate", coordinateValue);
					//showXY(event.getX(),event.getY());
				}
				return true;
			}
		});*/

        /**
         * class desc: 萸僻鳳祥謎弇离釴梓硉
         * <p>Copyright: Copyright(c) 2016 </p>
         * @Version 1.0
         * @Author Ethan
         */
        view.addView(drawView);

        drawView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    int intrinsicHeight = 0;
                    int intrinsicWidth = 0;
                    float offsetX = 0;
                    float offsetY = 0;

                    if (image.getDrawable() != null) {
                        //芞釴梓蛌遙
                        Drawable drawable = image.getDrawable();
                        Matrix matrix = image.getImageMatrix();
                        float[] values = new float[9];
                        matrix.getValues(values);

                        //場宎趙bitmap腔遵詢
                        intrinsicHeight = drawable.getIntrinsicHeight();
                        intrinsicWidth = drawable.getIntrinsicWidth();

                        offsetX = intrinsicWidth * values[0];
                        offsetY = intrinsicHeight * values[4];
                        float tempX = event.getX() / offsetX;
                        float tempY = event.getY() / offsetY;

                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        String X = decimalFormat.format(tempX);
                        String Y = decimalFormat.format(tempY);

                        coordinateValue = X + "," + Y;
                    }
                    //餅秶埴萸
                    //if((event.getX()>0 && event.getX()<offsetX )&&
                    //   (event.getY()>0 && event.getY()<offsetY )){

                    drawView.currentX = event.getX();
                    drawView.currentY = event.getY();
                    //笭餅
                    drawView.invalidate();
                    showXY(event.getX(), event.getY(), intrinsicWidth, intrinsicHeight);
                    //}
                }
                //殿隙true桶隴呾岆源楊眒冪揭燴蜆岈璃
                return true;
            }
        });
    }

// OK roi anh
    private boolean onEmployeeCommit(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && flag2 < 1) {

            String strEMP = passwd.getText().toString().trim();

            if (strEMP.length() == 0) {

                Toast.makeText(MainActivity.this,
                        "EMP IS NULL,PLEASE INPUT EMP!", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (strEMP.contains("\r\n")) {

                //忑珂裁隙陬遙俴賦帣睫
                Pattern CRLF = Pattern.compile("(\r\n)");
                Matcher m = CRLF.matcher(strEMP);
                if (m.find()) {
                    strEMP = m.replaceAll("");
                }
            }

            //換怀腔杅擂跡宒ㄩEMP<>VI
            //String data = "V0202064" + "@" + "VI";
            String data = strEMP + "@" + groupValue;

            AsyncTaskCheckEMP checkEmp = new AsyncTaskCheckEMP();
            checkEmp.execute(data);
            flag2++;
        }
        return false;
    }

    /**
     * class desc: VI Group恁
     * <p>Copyright: Copyright(c) 2016 </p>
     *
     * @Version 1.0
     * @Author Ethan
     */
    public class OnGroupSelectedListener implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View arg1, int arg2,
                                   long arg3) {

            groupValue = parent.getItemAtPosition(arg2).toString();
            Log.d("Group", groupValue);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /**
     * class desc: 禸鏡莉SN
     * <p>Copyright: Copyright(c) 2016 </p>
     *
     * @Version 1.0
     * @Author Ethan
     */
    public class checkSNListenerImpl implements OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (keyCode == KeyEvent.KEYCODE_ENTER && flag1 < 1) {

                //String strSN = "F09FC2B6FE0D";
                String strSN = sn.getText().toString().trim();

                if (strSN.length() == 0) {
                    Toast.makeText(MainActivity.this,
                            "SN IS NULL, PLEASE INPUT SN!", Toast.LENGTH_SHORT).show();
                    return false;
                }

                //瓚剿釬珛埜岆瘁衄癹
                if (!havePermisson) {
                    new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.btn_star)
                            .setTitle("恲黹枑尨").setMessage("THIS EMP NO HAVE PERMISSON")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            }).show();// show竭壽瑩

                    return false;
                }

                if (strSN.contains("\r\n")) {

                    //忑珂裁隙陬遙俴賦帣睫
                    Pattern CRLF = Pattern.compile("(\r\n)");
                    Matcher m = CRLF.matcher(strSN);
                    if (m.find()) {
                        strSN = m.replaceAll("");
                    }
                }

                //雄盄最狟婥莉芞ㄛ樓婥ErrorCode
                AsyncTaskCheckSN checkSN = new AsyncTaskCheckSN();
                checkSN.execute(strSN);
                flag1++;
            }
            return false;
        }
    }

    /**
     * class desc: 禸鏡埜馱馱瘍癹
     * <p>Copyright: Copyright(c) 2016 </p>
     *
     * @Version 1.0
     * @Author Ethan
     */
    public class checkEmpListenerImpl implements OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            Log.d("EMP", "Clickerkjkjjjll");
            if (keyCode == KeyEvent.KEYCODE_ENTER && flag2 < 1) {

                String strEMP = passwd.getText().toString().trim();

                if (strEMP.length() == 0) {

                    Toast.makeText(MainActivity.this,
                            "EMP IS NULL,PLEASE INPUT EMP!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (strEMP.contains("\r\n")) {

                    //忑珂裁隙陬遙俴賦帣睫
                    Pattern CRLF = Pattern.compile("(\r\n)");
                    Matcher m = CRLF.matcher(strEMP);
                    if (m.find()) {
                        strEMP = m.replaceAll("");
                    }
                }

                //換怀腔杅擂跡宒ㄩEMP<>VI
                //String data = "V0202064" + "@" + "VI";
                String data = strEMP + "@" + groupValue;

                AsyncTaskCheckEMP checkEmp = new AsyncTaskCheckEMP();
                checkEmp.execute(data);
                flag2++;
            }
            return false;
        }
    }

    /**
     * class desc:   場宎趙UI諷璃
     * <p>Copyright: Copyright(c) 2016 </p>
     *
     * @Version 1.0
     * @Author Ethan
     */
    public void initUI() {

        group = (Spinner) findViewById(R.id.spinner_Group);
        listView = (ListView) findViewById(R.id.listView);
        txtCode = (TextView) super.findViewById(R.id.txt_selectedCode);
        txtFace = (TextView) super.findViewById(R.id.txt_productFace);
        XYPointer = (TextView) super.findViewById(R.id.txtXYPointer);
        image = (ImageView) findViewById(R.id.image1);
        failBtn = (ImageView) findViewById(R.id.iv_fail_btn);
        passBtn = (ImageView) findViewById(R.id.iv_pass_btn);
        searchBtn = (ImageView) findViewById(R.id.iv_search_btn);
        settingBtn = (ImageView) findViewById(R.id.iv_setting_btn);
        passwd = (EditText) findViewById(R.id.txtEMP);
        sn = (EditText) findViewById(R.id.txtSN);
        pn = (TextView) findViewById(R.id.txtPN);
        mo = (TextView) findViewById(R.id.txtMO);
        passQty = (TextView) findViewById(R.id.txt_PassQty);
        failQty = (TextView) findViewById(R.id.txt_FailQty);
        failRate = (TextView) findViewById(R.id.txt_FailRate);
        connection = (TextView) findViewById(R.id.txt_Connection);
        serverIP = (EditText) findViewById(R.id.txtSERVER);
        currentTime = (TextView) findViewById(R.id.txtDATE);

    }

    /**
     * class desc:   ListView諷璃等僻岈璃揭燴滲杅
     * <p>Copyright: Copyright(c) 2016 </p>
     *
     * @Version 1.0
     * @Author Ethan
     */
    private class OnItemClickListenerImpl implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            @SuppressWarnings("unchecked")
            Map<String, String> map = (Map<String, String>) MainActivity.this.adapter
                    .getItem(position);

            errorCode1 = map.get("CODE1");
            errorCode2 = map.get("CODE2");
            txtCode.setText(errorCode2);
            System.out.println(errorCode1);
        }
    }

    /**
     * class desc:   刲坰偌聽萸僻岈璃揭燴滲杅
     * <p>Copyright: Copyright(c) 2016 </p>
     *
     * @Version 1.0
     * @Author Ethan
     */
    public class SearchBtnListener implements OnClickListener {

        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            intent.setClass(MainActivity.this, TableauActivity.class);
            MainActivity.this.startActivity(intent);
        }
    }

    /**
     * class desc:   Pass偌聽萸僻岈璃揭燴滲杅
     * Copyright: Copyright(c) 2016
     *
     * @Version 1.0
     * @Author Ethan
     */
    public class passBtnListener implements OnClickListener {

        //隅砱杅擂腔換怀跡宒
        //Pass: -->SFC : SN$鰭��$馱��$PASS      (趼睫揹��)
        //SFC-->:1.SN+PASS                      (趼睫揹��)
        //       2.囮腔埻秪

        @Override
        public void onClick(View v) {
            //�巕�馱馱����
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this); // 斐膘弝芞甜扢离奻狟恅
            final View view = layoutInflater.inflate(R.layout.dialog, null); // 鳳list_item票擁恅璃腔弝芞(趼睫揹��)
            new AlertDialog.Builder(MainActivity.this).setTitle("Please Comfirm").setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strSN, strStationName, strEmp;

                    strSN = sn.getText().toString().trim();
                    strStationName = "VI_2_101";
                    //strEmp = passwd.getText().toString().trim();
                    //passData = "KLU68VC@PT_2_014@MING!!@PASS";

                    if (strSN.isEmpty()) {
                        Toast.makeText(MainActivity.this, "SN IS NULL", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    passData = strSN + "@" + strStationName + "@" + emp + "@" + groupValue + "@" + "PASS";
                    //passData = strSN + "@" + strStationName + "@" + strEmp + "@" + "PASS";

                    Log.d("PassData", passData);
                    //雄盄最
                    AsyncTaskOverStation pass = new AsyncTaskOverStation();
                    pass.execute(passData);

                }
            }).setNegativeButton("Cancel", null).show();
        }
    }

    /**
     * class desc: Fail偌聽萸僻岈璃揭燴滲杅
     * Copyright: Copyright(c) 2016
     *
     * @Version 1.0
     * @Author Ethan
     */
    public class failBtnListener implements OnClickListener {

        //Fail:-->SFC: SN$怢��$馱��$祥謎測徨$醱$釱$FAIL    (趼睫揹��)
        //SFC-->1.SN+PASS                                  (趼睫揹��)
        //      2.囮腔埻秪                                                                   (趼睫揹��)

        @Override
        public void onClick(View v) {

            //�巕�馱馱����
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this); // 斐膘弝芞甜扢离奻狟恅
            final View view = layoutInflater.inflate(R.layout.dialog1, null); // 鳳list_item票擁恅璃腔弝芞
            new AlertDialog.Builder(MainActivity.this).setTitle("Please Comfirm").setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String strSN, strStationName, strEmp;
                    strSN = sn.getText().toString().trim();
                    strStationName = "VI_2_101";
                    //strEmp = passwd.getText().toString().trim();

                    if (strSN.isEmpty()) {
                        Toast.makeText(MainActivity.this, "SN峈IS NULL", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (errorCode1 == null) {
                        Toast.makeText(MainActivity.this, "ERROR CODE IS NULL!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //failData = "KMO3315@PT_2_014@MING!!@VP28@Back@0.5,0.3@FAIL";
                    failData = strSN + "@" + strStationName + "@" + emp + "@" + errorCode1 + "@" +
                            productFace + "@" + coordinateValue + "@" + groupValue + "@" + "FAIL";

                    //failData = strSN +"@"+ strStationName +"@"+ strEmp +"@"+ errorCode1 +"@"+
                    //		   productFace +"@"+ coordinateValue +"@"+ "FAIL";

                    Log.d("FailData", failData);
                    AsyncTaskOverStation fail = new AsyncTaskOverStation();
                    fail.execute(failData);
                }
            }).setNegativeButton("Cancel", null).show();
        }
    }

    /**
     * class desc:   Setting偌聽萸僻岈璃揭燴滲杅
     * Copyright: Copyright(c) 2016
     *
     * @Version 1.0
     * @Author Ethan
     */
    public class settingBtnListener implements OnClickListener {

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            MainActivity.this.startActivity(intent);
        }
    }

    /**
     * class desc: 鳳祥謎莉芞ErrorCode
     * <p>Copyright: Copyright(c) 2016 </p>
     *
     * @Version 1.0
     * @Author Ethan
     */
    public void addErrorCodeList(List<HashMap<String, String>> list) {

        listView.setVisibility(View.VISIBLE);
        list.remove(0);
        adapter = new SimpleAdapter(
                MainActivity.this,
                list,
                R.layout.errorcode_list,
                new String[]{"CODE2"},
                new int[]{R.id.txtErrorCode});

        listView.setAdapter(adapter);

    }

    /**
     * class desc:   場宎趙Gallery諷璃ㄛ樓婥厙釐芞
     * <p>Copyright: Copyright(c) 2016 </p>
     *
     * @throws IOException
     * @Version 1.0
     * @Author Ethan
     */
    private void initGallery(List<HashMap<String, String>> list) throws IOException {
        //忑珂樓徭掛華腔蘇庲
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.default_movie_post);

        imagesCache.put("background_non_load", bitmap); // 扢离遣湔笢蘇腔芞
        gallery = (Gallery) findViewById(R.id.Gallery1);
        urls.clear();

        String strBack, strBottom, strFront, strLeft, strRight, strTop;
        //VM
        String strServerIP = "10.224.134.93";
        //String strServerIP = "10.120.176.143/TableATA";
        //String strServerIP = "10.120.171.122";

        String strMO = list.get(1).get("PN").toString().trim();

        strBack = "http://" + strServerIP + "/product/" + strMO + "/" + "back.jpg";
        strBottom = "http://" + strServerIP + "/product/" + strMO + "/" + "bottom.jpg";
        strFront = "http://" + strServerIP + "/product/" + strMO + "/" + "front.jpg";
        strLeft = "http://" + strServerIP + "/product/" + strMO + "/" + "left.jpg";
        strRight = "http://" + strServerIP + "/product/" + strMO + "/" + "right.jpg";
        strTop = "http://" + strServerIP + "/product/" + strMO + "/" + "top.jpg";

        urls.add(strBack);
        urls.add(strBottom);
        urls.add(strFront);
        urls.add(strLeft);
        urls.add(strRight);
        urls.add(strTop);

        imageAdapter = new ImageAdapter(urls, this);
        gallery.setAdapter(imageAdapter);
        gallery.setOnItemClickListener(this);

        gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                num = position;
                Log.i("Ethan", "ItemSelected==" + position);

                GalleryWhetherStop();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("Ethan-Gallery", "菴" + position + "跺掩萸僻賸");

        switch (position) {
            case 0:
                productFace = "Back";
                txtFace.setText("Back");
                break;
            case 1:
                productFace = "Bottom";
                txtFace.setText("Bottom");
                break;
            case 2:
                productFace = "Front";
                txtFace.setText("Front");
                break;
            case 3:
                productFace = "Left";
                txtFace.setText("Left");
                break;
            case 4:
                productFace = "Right";
                txtFace.setText("Right");
                break;
            case 5:
                productFace = "Top";
                txtFace.setText("Top");
                break;
            default:
                break;
        }
        Log.d("Product", productFace);
        LoadImageTask loadImages = new LoadImageTask();
        image.setImageBitmap(loadImages.doInBackground(urls.get(position)));

    }

    /**
     * 瓚剿Gallery幗雄岆瘁礿砦,彆礿砦寀樓婥絞珜醱腔芞
     */
    private void GalleryWhetherStop() {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    int index = 0;
                    index = num;
                    Thread.sleep(1000);
                    if (index == num) {
                        url.add(urls.get(num));
                        if (num != 0 && urls.get(num - 1) != null) {
                            url.add(urls.get(num - 1));
                        }
                        if (num != urls.size() - 1 && urls.get(num + 1) != null) {
                            url.add(urls.get(num + 1));
                        }
                        Message m = new Message();
                        m.what = 1;
                        mHandler.sendMessage(m);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    // 樓婥芞腔祑祭昢
    class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onCancelled() {

            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                String url = params[0];
                boolean isExists = Files.compare(url);
                if (isExists == false) {
                    Net net = new Net();
                    byte[] data = net.downloadResource(MainActivity.this, url);
                    bitmap = BitmapFactory
                            .decodeByteArray(data, 0, data.length);
                    imagesCache.put(url, bitmap); // 參狟婥疑腔芞悵湔善遣湔笢
                    Files.saveImage(url, data);
                } else {
                    byte[] data = Files.readImage(url);
                    bitmap = BitmapFactory
                            .decodeByteArray(data, 0, data.length);
                    imagesCache.put(url, bitmap); // 參狟婥疑腔芞悵湔善遣湔笢
                }

                Message m = new Message();
                m.what = 0;
                mHandler.sendMessage(m);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 0: {
                        imageAdapter.notifyDataSetChanged();
                        break;
                    }
                    case 1: {
                        for (int i = 0; i < url.size(); i++) {
                            LoadImageTask task = new LoadImageTask();// 祑祭樓婥芞
                            task.execute(url.get(i));
                            Log.i("mahua", url.get(i));
                        }
                        url.clear();
                    }
                }
                super.handleMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 偌殿隙瑩豖堤粟堤勤趕遺
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.btn_dialog)
                    .setTitle("恲黹枑尨")
                    .setMessage("蠟隅堤繫ˋ")
                    .setPositiveButton("隅",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    dialog.dismiss();
                                    MainActivity.this.finish();
                                }
                            })
                    .setNegativeButton("秏",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    dialog.dismiss();
                                }
                            }).show();// show竭壽瑩
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * * 瓚剿埜馱馱瘍岆瘁衄癹
     * * @param url 渾瓚剿腔URL
     * * @return true: 褫湛 false: 祥褫湛
     */
    public class AsyncTaskCheckEMP extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... emp) {

            //count2=0;
            if (isCancelled()) {
                return null;
            }

            if (isConnect(webserviceUrl)) {

                String str[] = emp[0].split("@");

                //datalist = dbUtil.Checkemp("MING56!!","VI");
                datalist = dbUtil.Checkemp(str[0], str[1]);
                Log.d("Ethan", "Do in function doInBackground!");
                return datalist;
            } else {

                return null;
            }
        }


        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
            super.onPostExecute(result);

            Log.d("Ethan", "Do in function onPostExecute!");
            flag2 = 0;

            try {

                if (result == null) {

                    passwd.requestFocus();
                    connection.setText("  Connection : Fail");
                    havePermisson = false;
                    Toast.makeText(MainActivity.this, "厙CONNECT FAIL", Toast.LENGTH_SHORT).show();
                    return;
                } else if (result.size() == 1) {

                    //Toast.makeText(MainActivity.this, "杅擂祑都ㄐ", Toast.LENGTH_SHORT).show();
                    passwd.requestFocus();
                    connection.setText("  Connection : OK");
                    havePermisson = false;
                    return;
                }

                //MING56!!
                String strTemp = result.get(1).get("Emp").toString().trim();
                if (strTemp.contains("PASS")) {

                    String str[] = strTemp.split("@");
                    emp = str[0];
                    //蔚癹扢离峈true
                    havePermisson = true;
                    Log.d("OK", emp);
                    Toast.makeText(MainActivity.this, "EMP LOGIN OK", Toast.LENGTH_SHORT).show();
                    sn.requestFocus();//泐蛌善SN恅掛遺
                } else {

                    Log.d("Error", result.get(1).get("Emp").toString().trim());
                    havePermisson = false;

                    new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.btn_star)
                            .setTitle("恲黹枑尨").setMessage("Error Msg: " + result.get(1).get("Emp").toString())
                            .setNegativeButton("隅", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    passwd.setText("");
                                    passwd.requestFocus();
                                }
                            }).show();// show竭壽瑩
                }
                connection.setText("  Connection : OK");
            } catch (Exception e) {

                connection.setText("  Connection : Fail");
                e.printStackTrace();
            }
        }
    }

    /**
     * * 樓婥莉芞ㄛErrorCode
     * * @param url 渾瓚剿腔URL
     * * @return true: 褫湛 false: 祥褫湛
     */
    public class AsyncTaskCheckSN extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... sn) {

            //count1=0;
            if (isCancelled()) {
                return null;
            }

            if (isConnect(webserviceUrl)) {

                datalist = dbUtil.VIErrorCode(sn[0]);
                //datalist = dbUtil.VIErrorCode("802AA878959D");

                Log.d("Ethan", "Do in function doInBackground!");
                return datalist;
            } else {

                Log.d("Ethan", "Connection Error!");

                return null;
            }
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
            super.onPostExecute(result);

            Log.d("Ethan", "Do in function onPostExecute!");
            flag1 = 0;
            try {
                if (result == null) {

                    sn.requestFocus();
                    connection.setText("  Connection : Fail");
                    Toast.makeText(MainActivity.this, "厙釐麼督昢堤珋祑都ㄛ潰脤ㄐ", Toast.LENGTH_SHORT).show();
                    return;
                } else if (result.size() == 1) {//桶尨羶衄鳳善睡杅擂

                    //Toast.makeText(MainActivity.this, "杅擂祑都ㄐ", Toast.LENGTH_SHORT).show();
                    sn.requestFocus();
                    return;
                } else if (result.size() == 2) {//桶尨渣昫

                    String errorMsg = result.get(1).get("ERROR").toString();
                    Toast.makeText(MainActivity.this,
                            errorMsg, Toast.LENGTH_SHORT).show();
                } else {//彶善衄虴杅擂

                    //場宎趙Gallery諷璃ㄛ樓婥莉芞
                    initGallery(result);
                    //樓婥ErrorCode
                    addErrorCodeList(result);
                }

                if (result.get(1).get("PN") != null && !result.get(1).get("PN").isEmpty()) {

                    pn.setText(result.get(1).get("PN").toString().trim());
                }
                if (result.get(1).get("MO") != null && !result.get(1).get("MO").isEmpty()) {

                    mo.setText(result.get(1).get("MO").toString().trim());
                }

                connection.setText("  Connection : OK");

            } catch (Exception e) {

                connection.setText("  Connection : Fail");
                e.printStackTrace();
            }

            sn.requestFocus();
        }
    }

    /**
     * * PASS/FAIL徹桴杅擂揭燴
     * * @param url 渾瓚剿腔URL
     * * @return true: 褫湛 false: 祥褫湛
     */
    public class AsyncTaskOverStation extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... data) {

            if (isCancelled()) {
                return null;
            }

            if (isConnect(webserviceUrl)) {

                datalist = dbUtil.VIOverStation(data[0]);

                Log.d("Ethan", "Do in function doInBackground!");
                return datalist;
            } else {

                Log.d("Ethan", "Connection Error!");
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
            super.onPostExecute(result);

            try {
                if (result == null) {//桶尨厙釐麼氪督昢堤珋祑都

                    sn.setText("");
                    Toast.makeText(MainActivity.this, "厙釐麼督昢堤珋祑都ㄛ潰脤ㄐ", Toast.LENGTH_SHORT).show();
                    return;
                } else if (result.size() == 1) {//桶尨羶衄諉彶善衄虴杅擂

                    //Toast.makeText(MainActivity.this, "杅擂祑都ㄐ", Toast.LENGTH_SHORT).show();
                    sn.setText("");
                    return;
                }

                if (result.contains("PASS")) {//SN揭燴祥籵徹腔錶

                    Toast.makeText(MainActivity.this,
                            "OK", Toast.LENGTH_SHORT).show();
                } else {

                    String errorMsg = result.get(1).get("DATA").toString();
                    Toast.makeText(MainActivity.this,
                            errorMsg, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            sn.setText("");
            Log.d("Ethan", "Do in function onPostExecute!");
        }
    }


    /**
     * * 瓚剿URL岆瘁褫湛
     * * @param url 渾瓚剿腔URL
     * * @return true: 褫湛 false: 祥褫湛
     */
    public boolean isConnect(String urlStr) {
        int counts = 0;
        HttpURLConnection con = null;

        if (urlStr == null || urlStr.length() <= 0) {
            return false;
        }
        while (counts < 1) {
            try {
                URL url = new URL(urlStr);
                con = (HttpURLConnection)
                        url.openConnection();
                con.setConnectTimeout(1000);
                int state = con.getResponseCode();
                System.out.println(counts + "= " + state);
                if (state == 200) {

                    Log.d("Ethan", "URL褫蚚ㄐ");
                    return true;
                }
                break;
            } catch (Exception ex) {
                counts++;
                System.out.println("URL祥褫蚚ㄛ蟀諉菴 " + counts + " 棒");
                Log.d("Ethan", "URL祥褫蚚ㄐ");
                continue;
            } finally {
                if (con != null) {

                    con.disconnect();
                }
            }
        }
        return false;
    }

    //陂善釱ㄛ筳俴瓚
    private void showXY(float x, float y, int width, int height) {

        // && x<width && y<height
        if (x > 0 && y > 0) {

            XYPointer.setText("X Point" + x + " , Y Point" + y);
        } else {

            XYPointer.setText("弇离郖虴");
        }
    }
}
