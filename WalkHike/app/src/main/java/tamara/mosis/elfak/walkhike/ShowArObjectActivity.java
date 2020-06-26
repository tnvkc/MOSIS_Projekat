package tamara.mosis.elfak.walkhike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;

import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;

public class ShowArObjectActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private Scene scene;
    private Camera camera;

    private int objectType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_show_ar_object);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ar_fragment);

        //hide hand animation
        arFragment.getPlaneDiscoveryController().hide();
        arFragment.getPlaneDiscoveryController().setInstructionView(null);

        scene = arFragment.getArSceneView().getScene();
        camera = scene.getCamera();

        //procitati objectType koji stize preko intent-a otvaranju activity-ja:
        Bundle bundle = getIntent().getExtras();
        objectType = bundle.getInt("object_id");

        switch(objectType) {
            case 1: {
                ModelRenderable
                        .builder()
                        .setSource(this, R.raw.trophy)
                        .build()
                        .thenAccept(modelRenderable -> {

                            Node node = new Node();
                            node.setRenderable(modelRenderable);
                            scene.addChild(node);

                            node.setWorldPosition(new Vector3(0.0f, 0.5f, -2.0f));
                        });
                break;
            }
            case 2: {
                ModelRenderable
                        .builder()
                        .setSource(this, R.raw.heart)
                        .build()
                        .thenAccept(modelRenderable -> {

                            Node node = new Node();
                            node.setRenderable(modelRenderable);
                            scene.addChild(node);

                            node.setWorldPosition(new Vector3(0.0f, 0.5f, -2.0f));
                        });
                break;
            }
            case 3: {
                ModelRenderable
                        .builder()
                        .setSource(this, R.raw.marker)
                        .build()
                        .thenAccept(modelRenderable -> {

                            Node node = new Node();
                            node.setRenderable(modelRenderable);
                            scene.addChild(node);

                            node.setWorldPosition(new Vector3(0.0f, 0.5f, -2.0f));
                        });
                break;
            }
        }

    }
}
