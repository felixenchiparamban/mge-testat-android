package ch.hsr.mge.gadgeothek.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.mge.gadgeothek.R;
import ch.hsr.mge.gadgeothek.domain.Gadget;
import ch.hsr.mge.gadgeothek.helper.ViewHelper;
import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;

public class NewReservationFragment extends DialogFragment {
    private List<Gadget> gadgets;

    private ListView lvGadgets;
    private int selectedPos = -1;

    private NewReservationDialogListener callback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.gadgets = new ArrayList<>();
    }

    public static NewReservationFragment newInstance() {
        NewReservationFragment fragment = new NewReservationFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if (!(activity instanceof NewReservationDialogListener)) {
            throw new IllegalStateException("Activity must implement NewReservationDialogListener");
        }
        callback = (NewReservationDialogListener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Reserve your gadget");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_new_reservation, null);
        lvGadgets = rootView.findViewById(R.id.lvGadgets);
        lvGadgets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPos = i;
            }
        });

        loadGadgetsAsync(rootView);

        builder.setView(rootView);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton("Reserve", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                reserve();
            }
        });

        return builder.create();
    }

    private void loadGadgetsAsync(View rootView) {
        LibraryService.getGadgets(new Callback<List<Gadget>>() {
            @Override
            public void onCompletion(List<Gadget> input) {
                if (input.size() > 0) {
                    gadgets = input;
                    String[] gadgetDescriptions = ViewHelper.getGadgetsForDialog(input);
                    ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_list_item_single_choice, gadgetDescriptions);
                    lvGadgets.setAdapter(itemsAdapter);

                    lvGadgets.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(), "No gadgets to reserve", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void reserve() {
        if (selectedPos >= 0) {
            final Gadget gadget = gadgets.get(selectedPos);
            LibraryService.reserveGadget(gadget, new Callback<Boolean>() {
                @Override
                public void onCompletion(Boolean input) {
                    callback.onReservationCompletion(gadget, input);
                }

                @Override
                public void onError(String message) {
                    callback.onReservationError(message);
                }
            });
        } else {
            Toast.makeText(getActivity(), "No gadgets selected", Toast.LENGTH_LONG).show();
        }
    }

    public interface NewReservationDialogListener {
        public void onReservationCompletion(Gadget gadget, boolean success);

        public void onReservationError(String message);
    }
}
