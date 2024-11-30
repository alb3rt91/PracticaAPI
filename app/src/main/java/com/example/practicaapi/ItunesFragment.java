package com.example.practicaapi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.practicaapi.databinding.FragmentItunesBinding;
import com.example.practicaapi.databinding.ViewholderContenidoBinding;

import java.util.List;


public class ItunesFragment extends Fragment {
    private FragmentItunesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentItunesBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializa el adaptador
        ContenidosAdapter contenidosAdapter = new ContenidosAdapter();
        binding.recyclerviewContenidos.setAdapter(contenidosAdapter);

        // Configura el ViewModel
        ItunesViewModel itunesViewModel = new ViewModelProvider(this).get(ItunesViewModel.class);

        // Configura el SearchView
        binding.texto.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { return false; }

            @Override
            public boolean onQueryTextChange(String s) {
                itunesViewModel.buscar(s);
                return false;
            }
        });

        // Observa los cambios en los datos
        itunesViewModel.respuestaMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Itunes.Respuesta>() {
            @Override
            public void onChanged(Itunes.Respuesta respuesta) {
                if (respuesta != null && respuesta.results != null) {
                    contenidosAdapter.establecerListaContenido(respuesta.results);
                } else {
                    Log.e("ItunesFragment", "No se encontraron resultados o la respuesta es nula.");
                }
            }
        });
    }


    static class ContenidoViewHolder extends RecyclerView.ViewHolder {
        ViewholderContenidoBinding binding;

        public ContenidoViewHolder(@NonNull ViewholderContenidoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    class ContenidosAdapter extends RecyclerView.Adapter<ContenidoViewHolder>{
        List<Itunes.Contenido> contenidoList;

        @NonNull
        @Override
        public ContenidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ContenidoViewHolder(ViewholderContenidoBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ContenidoViewHolder holder, int position) {
            Itunes.Contenido contenido = contenidoList.get(position);

            holder.binding.title.setText(contenido.trackName);
            holder.binding.artist.setText(contenido.artistName);
            Glide.with(requireActivity()).load(contenido.artworkUrl100).into(holder.binding.artwork);
        }

        @Override
        public int getItemCount() {
            return contenidoList == null ? 0 : contenidoList.size();
        }

        void establecerListaContenido(List<Itunes.Contenido> contenidoList){
            this.contenidoList = contenidoList;
            notifyDataSetChanged();
        }
    }
}