package com.foxconn.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawView extends View{

	public float currentX=40; 
	public float currentY=50;
	
	public DrawView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

		@Override
    protected void onDraw(Canvas canvas) {       

        super.onDraw(canvas); 


        Paint paint=new Paint(); 
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2);

        paint.setColor(Color.RED); 


        canvas.drawCircle(currentX, currentY, 10, paint); 

    } 

}
