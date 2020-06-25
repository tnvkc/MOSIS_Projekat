package tamara.mosis.elfak.walkhike.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;

import tamara.mosis.elfak.walkhike.R;

public class AddObject_ShowInAR extends Fragment {

    private ArFragment arFragment;
    private Scene scene;
    private Camera camera;

    private int objectType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_object__show_ar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        arFragment = (ArFragment) getChildFragmentManager().findFragmentById(R.id.add_object_ar);

        //hide hand animation
        arFragment.getPlaneDiscoveryController().hide();
        arFragment.getPlaneDiscoveryController().setInstructionView(null);

        scene = arFragment.getArSceneView().getScene();
        camera = scene.getCamera();

        //procitati prosledjen objectType
        //Bundle bundle = getIntent().getExtras();
        //objectType = bundle.getInt("object_id");

        objectType = 1;

        switch(objectType) {
            case 1: {
                ModelRenderable
                        .builder()
                        .setSource(getContext(), R.raw.trophy)
                        .build()
                        .thenAccept(modelRenderable -> {

                            Node node = new Node();
                            node.setRenderable(modelRenderable);
                            scene.addChild(node);

                            node.setWorldPosition(new Vector3(0.0f, 0.2f, -3.0f));
                        });
                break;
            }
            case 2: {
                ModelRenderable
                        .builder()
                        .setSource(getContext(), R.raw.emoji_love)
                        .build()
                        .thenAccept(modelRenderable -> {

                            Node node = new Node();
                            node.setRenderable(modelRenderable);
                            scene.addChild(node);

                            node.setWorldPosition(new Vector3(0.0f, 0.2f, -3.0f));
                        });
                break;
            }
            case 3: {
                ModelRenderable
                        .builder()
                        .setSource(getContext(), R.raw.marker)
                        .build()
                        .thenAccept(modelRenderable -> {

                            Node node = new Node();
                            node.setRenderable(modelRenderable);
                            scene.addChild(node);

                            node.setWorldPosition(new Vector3(0.0f, 0.2f, -3.0f));
                        });
                break;
            }
        }
    }
}