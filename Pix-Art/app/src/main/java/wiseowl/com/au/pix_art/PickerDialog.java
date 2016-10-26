package wiseowl.com.au.pix_art;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

/**
 *  Created by anish.patel on 25/10/2016.
 */
public class PickerDialog extends DialogFragment {


    private DialogListItemPosListner posListner;
    private int value = 0;
    private AlertDialog.Builder builder;
    private String[] array;
    private int[] tileSize = new int[]{20,32,40,50};
    public static String PARAM = "STRING_ARRAY";
    private String defaultString;


    public static PickerDialog newInstance(String[] array, String defaultString, DialogListItemPosListner posListner) {
        PickerDialog pickerDialog = new PickerDialog();

        pickerDialog.posListner = posListner;
        pickerDialog.defaultString = defaultString;

        Bundle arg = new Bundle();
        arg.putStringArray(PARAM, array);
        pickerDialog.setArguments(arg);

        return pickerDialog;
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());

        if (getArguments() != null) {
            array = getArguments().getStringArray(PARAM);
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View v = inflater.inflate(R.layout.dialog_string_picker, null, false);
        final CustomPicker picker = (CustomPicker) v.findViewById(R.id.picker);
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        setDividerColor(picker, ContextCompat.getColor(getContext(), R.color.colorPrimary));


        int max = (array.length - 1);
        picker.setMinValue(0);
        picker.setMaxValue((max >=0 ? max : 0));
        picker.setWrapSelectorWheel(false);
        picker.setDisplayedValues(array);

        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(final NumberPicker numberPicker, final int i, final int i1) {
                value = i1;
            }
        });

        Button btn = (Button) v.findViewById(R.id.pass_change_confirm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if (posListner != null) posListner.getItemPos(tileSize[value]);
                PickerDialog.this.getDialog().cancel();
            }
        });

        int index = 0;
        if(defaultString != null) {
            for (int i = 0; i < array.length; i++) {
                if(array[i].compareTo(defaultString) == 0)
                    index = i;
            }
        }
        value = index;
        picker.setValue(index);

        builder.setView(v);

        return builder.create();
    }

    private void setDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }



    public interface DialogListItemPosListner {

        public void getItemPos(int pos);
    }


}
