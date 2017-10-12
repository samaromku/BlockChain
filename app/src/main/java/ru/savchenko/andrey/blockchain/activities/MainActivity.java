package ru.savchenko.andrey.blockchain.activities;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.savchenko.andrey.blockchain.R;
import ru.savchenko.andrey.blockchain.adapters.USDAdapter;
import ru.savchenko.andrey.blockchain.base.BaseActivity;
import ru.savchenko.andrey.blockchain.base.BaseRepository;
import ru.savchenko.andrey.blockchain.entities.USD;
import ru.savchenko.andrey.blockchain.interfaces.OnItemClickListener;
import ru.savchenko.andrey.blockchain.network.RequestManager;
import ru.savchenko.andrey.blockchain.repositories.USDRepository;

public class MainActivity extends BaseActivity implements OnItemClickListener {
    @BindView(R.id.constraintMain)LinearLayout constraintMain;
    @BindView(R.id.rvExchange)RecyclerView rvExchange;
    USDAdapter adapter;

    public static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initRv();
        RequestManager.getRetrofitService().getExchange()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(exchange -> {
                    String text = "Закупочная " + exchange.getUSD().getBuy() + "\n15 мин назад: " + exchange.getUSD().get5m();
                    Log.i(TAG, text);
                    new USDRepository().writeIdDb(exchange.getUSD());
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                }, Throwable::printStackTrace);
    }



    private void initRv() {
        rvExchange.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvExchange.getContext(), DividerItemDecoration.HORIZONTAL);
        rvExchange.addItemDecoration(dividerItemDecoration);
        adapter = new USDAdapter();
        adapter.setClickListener(this);
        adapter.setDataList(new BaseRepository<>(USD.class).getAll());
        rvExchange.setAdapter(adapter);
    }

    @Override
    public void onClick(int position) {
        Log.i(TAG, "onClick: " + position);
    }
}
