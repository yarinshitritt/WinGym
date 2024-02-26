//import android.content.Context;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//
//public class StepCounterUtil implements SensorEventListener {
//
//    private SensorManager sensorManager;
//    private Sensor stepCounterSensor;
//    private OnStepCountListener stepCountListener;
//
//    public StepCounterUtil(Context context) {
//        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
//        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//    }
//
//    public interface OnStepCountListener {
//        void onStepCount(int stepCount);
//    }
//
//    public void setStepCountListener(OnStepCountListener listener) {
//        stepCountListener = listener;
//    }
//
//    public void register() {
//        if (stepCounterSensor != null) {
//            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
//        }
//    }
//
//    public void unregister() {
//        sensorManager.unregisterListener(this);
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
//            int stepCount = (int) event.values[0];
//            if (stepCountListener != null) {
//                stepCountListener.onStepCount(stepCount);
//            }
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        // Not needed for step counter sensor
//    }
//}