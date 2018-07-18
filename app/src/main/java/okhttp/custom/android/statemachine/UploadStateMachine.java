package okhttp.custom.android.statemachine;

import android.os.Message;
import android.util.Log;

import okhttp.custom.android.statemachine.statemachine.IState;
import okhttp.custom.android.statemachine.statemachine.State;
import okhttp.custom.android.statemachine.statemachine.StateMachine;

public class UploadStateMachine extends StateMachine {

    private final static String TAG = "UploadStateMachine";

    public final static String Default = "Default";
    public final static String ReceiveUploadRequest = "ReceiveUploadRequest";
    public final static String Uploading = "Uploading";
    public final static String UploadFinish = "UploadFinish";

    private final static int MSG_DEFAULT = 0;
    private static final int CMD_REQUEST = 1;
    private static final int CMD_UPLOAD_ING = 2;
    private static final int CMD_FINISH = 3;

    private State mDefaultState;
    private State mReceiveRequestState;
    private State mUploadingState;
    private State mUploadFinishState;

    private UpdateStateListener updateStateListener;


    public UploadStateMachine() {
        super(TAG);
    }

    public void registerListener(UpdateStateListener l) {
        this.updateStateListener = l;
    }

    public void addAllStateToMachine() {

        mDefaultState = new Default();
        mReceiveRequestState = new ReceiveRequestState();
        mUploadingState = new UploadingState();
        mUploadFinishState = new UploadFinishState();

        addState(mDefaultState, null);
        addState(mReceiveRequestState, mDefaultState);
        addState(mUploadingState, mReceiveRequestState);
        addState(mUploadFinishState, mUploadingState);

        setInitialState(mDefaultState);
    }

    public IState currentState() {
        return getCurrentState();
    }

    class Default extends State {
        @Override
        public void enter() {
            Log.i(TAG, "UploadStateMachine Default--enter");
            notifyState(getName());
        }

        @Override
        public void exit() {
            Log.i(TAG, "UploadStateMachine Default--exit");
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.i(TAG, "UploadStateMachine Default--processMessage");
            switch (msg.what) {
                case MSG_DEFAULT:
                    transitionTo(mDefaultState);
                    break;
                case CMD_REQUEST:
                    transitionTo(mReceiveRequestState);
                    break;
                case CMD_UPLOAD_ING:
                    transitionTo(mUploadingState);
                    break;
                case CMD_FINISH:
                    transitionTo(mUploadFinishState);
                    break;
                default:
                    return false;
            }
            return true;
        }

        @Override
        public String getName() {
            return Default;
        }
    }

    class ReceiveRequestState extends State {
        @Override
        public void enter() {
            Log.i(TAG, "UploadStateMachine receiveRequestState--enter");
            notifyState(getName());
        }

        @Override
        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case CMD_REQUEST:
                    transitionTo(mReceiveRequestState);
                    Log.i(TAG, "UploadStateMachine receiveRequestState--processMessage");
                    sendMessage(obtainMessage(CMD_UPLOAD_ING));
                    return HANDLED;
                default:
                    return NOT_HANDLED;
            }
        }

        @Override
        public void exit() {
            super.exit();
            Log.i(TAG, "UploadStateMachine receiveRequestState--exit");
        }

        @Override
        public String getName() {
            return ReceiveUploadRequest;
        }
    }

    class UploadingState extends State {

        @Override
        public void enter() {
            super.enter();
            Log.i(TAG, "UploadStateMachine UploadingState--enter");
            notifyState(getName());
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.i(TAG, "UploadStateMachine UploadingState--processMessage  msg = " + msg);
            switch (msg.what) {
                case CMD_UPLOAD_ING:
                    transitionTo(mUploadingState);
                    sendMessage(obtainMessage(CMD_FINISH));
                    return HANDLED;
                default:
                    return NOT_HANDLED;
            }
        }

        @Override
        public void exit() {
            super.exit();
            Log.i(TAG, "UploadStateMachine UploadingState--exit()");
        }

        @Override
        public String getName() {
            return Uploading;
        }
    }

    class UploadFinishState extends State {

        @Override
        public void enter() {
            super.enter();
            Log.i(TAG, "UploadStateMachine UploadFinishState--enter()");
            notifyState(getName());
        }

        @Override
        public boolean processMessage(Message msg) {
            Log.i(TAG, "UploadStateMachine UploadFinishState--processMessage() msg = " + msg);
            switch (msg.what) {
                case CMD_FINISH:
                    transitionTo(mUploadingState);
                    sendMessage(obtainMessage(MSG_DEFAULT));
                    return HANDLED;
                default:
                    return NOT_HANDLED;
            }
        }

        @Override
        public void exit() {
            super.exit();
            Log.i(TAG, "UploadStateMachine UploadFinishState--exit()");
        }

        @Override
        public String getName() {
            return UploadFinish;
        }
    }

    private void notifyState(String text) {
        if (updateStateListener != null) {
            updateStateListener.update(text);
        }
    }

    public void default_() {
        sendMessage(MSG_DEFAULT);
    }

    public void receiveUploadRequest() {
        sendMessage(CMD_REQUEST);
    }

    public void uploading() {
        sendMessage(CMD_UPLOAD_ING);
    }

    public void uploadingFinis() {
        sendMessage(CMD_FINISH);
    }

}
