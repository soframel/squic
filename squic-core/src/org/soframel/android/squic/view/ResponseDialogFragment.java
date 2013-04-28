package org.soframel.android.squic.view;

import org.soframel.android.squic.PlayQuizActivity;
import org.soframel.android.squic.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class ResponseDialogFragment extends DialogFragment {

	private static final String TAG = "Squic_ResponseDialogFragment";

	public ResponseDialogFragment() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// find arguments
		final PlayQuizActivity activity = (PlayQuizActivity) this.getActivity();
		String response = this.getArguments().getString("response");

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		String text = activity.getString(R.string.response_prefix) + " "
				+ response;
		builder.setMessage(text).setPositiveButton(R.string.response_ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Log.d(TAG, "Close dialog !");
						dialog.dismiss();
						activity.continueQuiz();
					}
				});

		// Create the AlertDialog object and return it
		return builder.create();
	}
}
