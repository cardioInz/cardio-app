package cardio_app.activity.statistics;

import android.app.AlertDialog;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;

import cardio_app.R;
import cardio_app.databinding.DialogEventBinding;
import cardio_app.db.model.Event;
import cardio_app.viewmodel.EventDataViewModel;

public class EventDialog extends DialogFragment {
    private static final String DATA = "data";
    private Event event;

    public static EventDialog newInstance(Event event) {
        EventDialog eventDialog = new EventDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(DATA, event);
        eventDialog.setArguments(bundle);

        return eventDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        event = getArguments().getParcelable(DATA);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();

        DialogEventBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_event, null, false);
        binding.setEvent(new EventDataViewModel(event));

        return new AlertDialog.Builder(activity)
                .setView(binding.getRoot())
                .setTitle(getResources().getString(R.string.dialog_title))
                .setPositiveButton(getResources().getString(R.string.dialog_ok), null)
                .create();
    }
}
