package com.example.rkmalik.resto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rkmalik.data.Category;
import com.example.rkmalik.data.FoodItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by shashankranjan on 4/7/15.
 */
public class BuildOrderPageTwoAdapter extends BaseExpandableListAdapter implements TextToSpeech.OnInitListener {
    private Activity _activity;
    private List<Category> _orderCategory;
    private int _restId;
    private TextToSpeech _tts;
    List<FoodItem> ingredients;
    Button _finishBtn;

    BuildOrderPageTwoAdapter(Activity activity, List<Category> orderCategory, int restId, TextToSpeech tts,
                             Button finishBtn){
        _activity = activity;
        _orderCategory = orderCategory;
        _restId = restId;
        _tts = tts;
        ingredients = new ArrayList<FoodItem>();
        _finishBtn = finishBtn;
    }

    @Override
    public int getGroupCount() {
       return this._orderCategory.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this._orderCategory.get(i).getFoodItems().size();
    }

    @Override
    public Object getGroup(int i) {
        return this._orderCategory.get(i).getName();
    }

    @Override
    public Object getChild(int i, int i2) {
        return this._orderCategory.get(i).getFoodItems().get(i2).getName();
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i2) {
        return i2;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if(!this.isListEmpty()){
            final String headerText = (String) getGroup(i);

            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader = (TextView) view.findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerText);
        }
        return view;
    }

    @Override
    public View getChildView(int grpPos, int childPos, boolean b, View convertView, ViewGroup viewGroup) {
        if(!isListEmpty()) {
            final FoodItem foodItem = _orderCategory.get(grpPos).getFoodItems().get(childPos);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.buildorder_page_two_item, null);
            }

            ImageView imgListChild = (ImageView) convertView.findViewById(R.id.buildorder_imageView2);
            imgListChild.setImageBitmap(foodItem.getMainImage());

            imgListChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent step2Intent = new Intent(_activity, BuildOrderPageTwo.class);
                    step2Intent.putExtra("id", foodItem.getId());
                    step2Intent.putExtra("restId", _restId);
                    _activity.startActivity(step2Intent);

                }
            });

            TextView txtListChild = (TextView) convertView.findViewById(R.id.builorder_lblListItem2);
            txtListChild.setText(foodItem.getName());
            txtListChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent step2Intent = new Intent(_activity, BuildOrderPageTwo.class);
                    step2Intent.putExtra("id", foodItem.getId());
                    step2Intent.putExtra("restId", _restId);
                    _activity.startActivity(step2Intent);
                }
            });

            TextView txtListAlias = (TextView) convertView.findViewById(R.id.buildorder_aliasName2);
            txtListAlias.setText(foodItem.getLocalName());
            txtListAlias.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent step2Intent = new Intent(_activity, BuildOrderPageTwo.class);
                    step2Intent.putExtra("id", foodItem.getId());
                    step2Intent.putExtra("restId", _restId);
                    _activity.startActivity(step2Intent);
                }
            });

            if(foodItem.getServingSize()!=null)
            {
                TextView txtListCalorie = (TextView) convertView.findViewById(R.id.buildorder_calorieInfo2);
                txtListCalorie.setText(foodItem.getCalories() + " calories per " + foodItem.getServingSize());
                txtListCalorie.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent detailIntent = new Intent(_activity, FoodItemDetailActivity.class);
                        detailIntent.putExtra("id", foodItem.getId());
                        detailIntent.putExtra("restId", _restId);
                        _activity.startActivity(detailIntent);
                    }
                });
            }


            ImageView imgVegNonVeg = (ImageView) convertView.findViewById(R.id.buildorder_image_vegnoveg2);
            imgVegNonVeg.setImageResource(foodItem.isVeg() ? R.drawable.veg_icon : R.drawable.non_veg_icon);

            final CheckBox imgFavButton = (CheckBox) convertView.findViewById(R.id.imageBtn_select);
            imgFavButton.setChecked(isFoodItemAdded(foodItem));
            imgFavButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(imgFavButton.isChecked()) {
                        Toast.makeText(_activity, foodItem.getName() + " is added to the order", Toast.LENGTH_SHORT).show();
                        ingredients.add(foodItem);
                    } else {
                        Toast.makeText(_activity, foodItem.getName() + " is removed from the order", Toast.LENGTH_SHORT).show();
                        ingredients.remove(foodItem);
                    }
                    if(ingredients.size() > 0){
                        _finishBtn.setEnabled(true);
                    } else {
                        _finishBtn.setEnabled(false);
                    }
                }
            });

            ImageView imgSpkButton = (ImageView) convertView.findViewById(R.id.buildorder_imageBtn_speaker2);
            imgSpkButton.setImageResource(R.drawable.icon_audio);
            imgSpkButton.setClickable(true);
            imgSpkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    speakOut(foodItem.getName());
                }
            });
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return false;
    }

    private boolean isListEmpty(){
        return (this._orderCategory.size() <= 0);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = _tts.setLanguage(Locale.US);

            // tts.setPitch(5); // set pitch level

            // tts.setSpeechRate(2); // set speech speed rate

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported");
            }
        } else {
            Log.e("TTS", "Initilization Failed");
        }
    }

    private void speakOut(String text) {
        AudioManager am = (AudioManager)_activity.getSystemService(Context.AUDIO_SERVICE);
        int amStreamMusicMaxVol = am.getStreamVolume(am.STREAM_VOICE_CALL);
        am.setStreamVolume(am.STREAM_VOICE_CALL, amStreamMusicMaxVol, 0);
        _tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);

    }

    public List<FoodItem> getIngredientsList(){
        return ingredients;
    }

    private boolean isFoodItemAdded(FoodItem foodItem){
        return ingredients.contains(foodItem);
    }
}
