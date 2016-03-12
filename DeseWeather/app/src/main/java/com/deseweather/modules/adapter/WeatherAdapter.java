package com.deseweather.modules.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deseweather.R;
import com.deseweather.modules.domain.Setting;
import com.deseweather.modules.domain.Weather;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by JOE on 2016/3/11.
 */
public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static String TAG = WeatherAdapter.class.getSimpleName();

    private Context mContext;
    private final int TYPE_ONE = 0;
    private final int TYPE_TWO = 1;
    private final int TYPE_THREE = 2;
    private final int TYPE_FOUR = 3;

    private Weather mWeatherData;
    private Setting mSetting;

    public WeatherAdapter(Context context, Weather weatherData) {
        mContext = context;
        mWeatherData = weatherData;

        mSetting = Setting.getInstance();
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case TYPE_ONE:
                return TYPE_ONE;
            case TYPE_TWO:
                return TYPE_TWO;
            case TYPE_THREE:
                return TYPE_THREE;
            case TYPE_FOUR:
                return TYPE_FOUR;
            default:
        }

        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ONE:
                return ;
            case TYPE_TWO:
                return TYPE_TWO;
            case TYPE_THREE:
                return TYPE_THREE;
            case TYPE_FOUR:
                return TYPE_FOUR;
            default:
        }

        return ;
    }


    //判断当前日期是星期几
    public static String day4Week(String pTime) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(pTime));
        int day4Week = 0;
        String day = "";
        day4Week = c.get(Calendar.DAY_OF_WEEK);
        switch (day4Week) {
            case 1:
                day = "星期日";
                break;
            case 2:
                day = "星期一";
                break;
            case 3:
                day = "星期二";
                break;
            case 4:
                day = "星期三";
                break;
            case 5:
                day = "星期四";
                break;
            case 6:
                day = "星期五";
                break;
            case 7:
                day = "星期六";
                break;
        }
        return day;
    }

    //当前天气情况
    public class NowWeatherViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView weatherIcon;
        private TextView tempFlu;
        private TextView tempMax;
        private TextView tempMin;

        private TextView tempPm;
        private TextView tempQuality;

        public NowWeatherViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            weatherIcon = (ImageView) itemView.findViewById(R.id.weather_icon);
            tempFlu = (TextView) itemView.findViewById(R.id.temp_flu);
            tempMax = (TextView) itemView.findViewById(R.id.temp_max);
            tempMin = (TextView) itemView.findViewById(R.id.temp_min);

            tempPm = (TextView) itemView.findViewById(R.id.temp_pm);
            tempQuality = (TextView) itemView.findViewById(R.id.temp_quality);
        }
    }

    class HoursWeatherViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout itemHourInfoLinearlayout;
        private TextView[] mClock = new TextView[mWeatherData.hourlyForecast.size()];
        private TextView[] mTemp = new TextView[mWeatherData.hourlyForecast.size()];
        private TextView[] mHumidity = new TextView[mWeatherData.hourlyForecast.size()];
        private TextView[] mWind = new TextView[mWeatherData.hourlyForecast.size()];


        public HoursWeatherViewHolder(View itemView) {
            super(itemView);
            itemHourInfoLinearlayout = (LinearLayout) itemView.findViewById(R.id.item_hour_info_linearlayout);

            for (int i = 0; i < mWeatherData.hourlyForecast.size(); i++) {
                View view = View.inflate(mContext, R.layout.item_hour_info_line, null);
                mClock[i] = (TextView) view.findViewById(R.id.one_clock);
                mTemp[i] = (TextView) view.findViewById(R.id.one_temp);
                mHumidity[i] = (TextView) view.findViewById(R.id.one_humidity);
                mWind[i] = (TextView) view.findViewById(R.id.one_wind);
                itemHourInfoLinearlayout.addView(view);
            }
        }
    }

    /**
     * 当日建议
     */
    class SuggestionViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView clothBrief;
        private TextView clothTxt;
        private TextView sportBrief;
        private TextView sportTxt;
        private TextView travelBrief;
        private TextView travelTxt;
        private TextView fluBrief;
        private TextView fluTxt;


        public SuggestionViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            clothBrief = (TextView) itemView.findViewById(R.id.cloth_brief);
            clothTxt = (TextView) itemView.findViewById(R.id.cloth_txt);
            sportBrief = (TextView) itemView.findViewById(R.id.sport_brief);
            sportTxt = (TextView) itemView.findViewById(R.id.sport_txt);
            travelBrief = (TextView) itemView.findViewById(R.id.travel_brief);
            travelTxt = (TextView) itemView.findViewById(R.id.travel_txt);
            fluBrief = (TextView) itemView.findViewById(R.id.flu_brief);
            fluTxt = (TextView) itemView.findViewById(R.id.flu_txt);
        }
    }

    /**
     * 未来天气
     */
    class ForecastViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout forecastLinear;
        private TextView[] forecastDate = new TextView[mWeatherData.dailyForecast.size()];
        private TextView[] forecastTemp = new TextView[mWeatherData.dailyForecast.size()];
        private TextView[] forecastTxt = new TextView[mWeatherData.dailyForecast.size()];
        private ImageView[] forecastIcon = new ImageView[mWeatherData.dailyForecast.size()];


        public ForecastViewHolder(View itemView) {
            super(itemView);
            forecastLinear = (LinearLayout) itemView.findViewById(R.id.forecast_linear);
            for (int i = 0; i < mWeatherData.dailyForecast.size(); i++) {
                View view = View.inflate(mContext, R.layout.item_forecast_line, null);
                forecastDate[i] = (TextView) view.findViewById(R.id.forecast_date);
                forecastTemp[i] = (TextView) view.findViewById(R.id.forecast_temp);
                forecastTxt[i] = (TextView) view.findViewById(R.id.forecast_txt);
                forecastIcon[i] = (ImageView) view.findViewById(R.id.forecast_icon);
                forecastLinear.addView(view);
            }
        }
    }

}
