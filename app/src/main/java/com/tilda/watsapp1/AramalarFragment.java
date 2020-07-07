package com.tilda.watsapp1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AramalarFragment extends Fragment {
//    private RecyclerView recyclerView;
//    private ArrayList<User> kisiler = new ArrayList<>();
//    private AdapterKisiler adapterKisiler;
//
//    private FirebaseAuth mAuth;
//    private FirebaseUser mUser;
//
//    private FirebaseDatabase db;
//    private DatabaseReference ref;
//    private ValueEventListener listener;

    public AramalarFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aramalar, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        recyclerView = view.findViewById(R.id.recyclerView_kisiler);
//        adapterKisiler = new AdapterKisiler(getActivity(), kisiler);
//        recyclerView.setAdapter(adapterKisiler);
//
//        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(manager);
//
//        mAuth = FirebaseAuth.getInstance();
//        mUser = mAuth.getCurrentUser();
//
//        db = FirebaseDatabase.getInstance();
//        ref = db.getReference("users");
//        listener = ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                kisiler.clear();
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    User u = data.getValue(User.class);
//                    u.seteMail(data.getKey());
//                    kisiler.add(u);
//                }
//                adapterKisiler.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}