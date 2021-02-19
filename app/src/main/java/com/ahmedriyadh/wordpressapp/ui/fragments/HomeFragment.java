package com.ahmedriyadh.wordpressapp.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedriyadh.wordpressapp.R;
import com.ahmedriyadh.wordpressapp.adapters.PostAdapter;
import com.ahmedriyadh.wordpressapp.api.ApiClient;
import com.ahmedriyadh.wordpressapp.databinding.FragmentHomeBinding;
import com.ahmedriyadh.wordpressapp.models.Post;
import com.ahmedriyadh.wordpressapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * By Ahmed Riyadh
 * Note : Please Don't Re-Post This Project Source Code on Social Media etc..
 * */

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private SessionManager sessionManager;
    private Context context;
    private Activity activity;
    private HomeFragmentListener listener;
    private RecyclerView recyclerView;
    private List<Post> postList;
    private PostAdapter adapter;
    private ProgressBar progressBar;
    private static final String TAG = "HomeFragment";

    public HomeFragment() {
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        context = getContext();
        activity = getActivity();


        initView();
        initVar();
        prepareRecyclerView();

        showProgressBar();
        getPosts(true);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void getPosts(boolean isShouldClearPostList) {
        ApiClient.getApiInterface().getPosts(10, true)
                .enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            if (!postList.isEmpty() && isShouldClearPostList) {
                                postList.clear();
                            }

                            postList.addAll(response.body());

                            if (postList.size() > 0) {
                                adapter.notifyDataSetChanged();
                            }

                        } else {
                            Toast.makeText(context, "تعذر تحميل المقالات1", Toast.LENGTH_SHORT).show();
                        }
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<List<Post>> call, Throwable t) {
                        hideProgressBar();
                        Toast.makeText(context, "تعذر تحميل المقالات2", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    private void initView() {
        recyclerView = binding.recyclerView;
        progressBar = binding.progressCircular;
    }

    private void prepareRecyclerView() {
        recyclerView.setHasFixedSize(true);
        adapter = new PostAdapter(context, postList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(View view, int position) {
                Toast.makeText(context, postList.get(position).getTitle().getRendered(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initVar() {
        sessionManager = new SessionManager(context);
        postList = new ArrayList<>();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (HomeFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " HomeFragmentListener is not implemented");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface HomeFragmentListener {
        void onLoggedOut();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                SessionManager.getInstance(context).logout();
                listener.onLoggedOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void showProgressBar() {
        if (progressBar.getVisibility() == View.GONE || progressBar.getVisibility() == View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressBar() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }
}
