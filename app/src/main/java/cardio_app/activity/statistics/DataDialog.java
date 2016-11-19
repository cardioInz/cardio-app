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
import cardio_app.databinding.DialogPressureBinding;
import cardio_app.db.model.PressureData;
import cardio_app.viewmodel.PressureDataViewModel;

public class DataDialog extends DialogFragment {
    private static final String DATA = "data";
    private PressureData data;

    public static DataDialog newInstance(PressureData data) {
        DataDialog dialog = new DataDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(DATA, data);
        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = getArguments().getParcelable(DATA);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();

        DialogPressureBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_pressure, null, false);
        binding.setData(new PressureDataViewModel(data));

        return new AlertDialog.Builder(activity)
                .setView(binding.getRoot())
                .setTitle(getResources().getString(R.string.dialog_title))
                .setPositiveButton(getResources().getString(R.string.dialog_ok), null)
                .create();
    }
}
