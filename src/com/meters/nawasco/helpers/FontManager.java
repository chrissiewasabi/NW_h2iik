package com.meters.nawasco.helpers;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class FontManager {
	private Context _context;
	
public FontManager(Context context){
	this._context = context;
}
public Typeface  LogoFont(){
	Typeface font = Typeface.createFromAsset(_context.getAssets(),
			"ClassicRobot.ttf");
	return font;
}

}
