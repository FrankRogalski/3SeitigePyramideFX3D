package tuncer.privat;

import java.awt.AWTException;
import java.awt.Robot;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.AmbientLight;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	
	private MeshView pyramid;
	private boolean pause = false;
	private PerspectiveCamera c;
	private double x = 0, y = 0;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void init() throws Exception {
		final double fps = 1000/60;
		Timeline tl_draw = new Timeline(new KeyFrame(Duration.millis(fps), e -> {
			this.draw();
		}));
		tl_draw.setCycleCount(Timeline.INDEFINITE);
		tl_draw.play();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
			System.exit(0);
		}
		
		final int komisch = 300;
		
		PointLight lightUp = new PointLight(Color.WHITE);
		lightUp.setTranslateY(-komisch);
		Sphere sphereUp = new Sphere(10);
		sphereUp.setTranslateY(-komisch);
		
		PointLight lightDown = new PointLight(Color.WHITE);
		lightDown.setTranslateY(komisch);
		Sphere sphereDown = new Sphere(10);
		sphereDown.setTranslateY(komisch);
		
		PointLight lightLeft = new PointLight(Color.WHITE);
		lightLeft.setTranslateX(-komisch);
		Sphere sphereLeft = new Sphere(10);
		sphereLeft.setTranslateX(-komisch);
		
		PointLight lightRight = new PointLight(Color.WHITE);
		lightRight.setTranslateX(komisch);
		Sphere sphereRight = new Sphere(10);
		sphereRight.setTranslateX(komisch);
		
		PointLight lightFront = new PointLight(Color.WHITE);
		lightFront.setTranslateZ(-komisch);
		Sphere sphereFront = new Sphere(10);
		sphereFront.setTranslateZ(-komisch);
		
		PointLight lightBack = new PointLight(Color.WHITE);
		lightBack.setTranslateZ(komisch);
		Sphere sphereBack = new Sphere(10);
		sphereBack.setTranslateZ(komisch);
		

		Group g1 = this.buildGraphics();
		g1.getChildren().addAll(sphereUp, sphereDown, sphereLeft, sphereRight, sphereFront, sphereBack, new AmbientLight());
		g1.getChildren().addAll(lightUp, lightDown, lightLeft, lightRight, lightFront, lightBack);
		
	    SubScene s1 = createSubScene(g1, 1000, 1000);
	    
	    HBox root = new HBox();
	    Scene scene = new Scene(root, s1.getWidth(), s1.getHeight(), true, SceneAntialiasing.BALANCED);
		
	    root.getChildren().addAll(s1);

		g1.setTranslateX(s1.getWidth()/2);
		g1.setTranslateY(s1.getHeight()/2);
	    
		root.getTransforms().add(new Rotate(0, Rotate.Y_AXIS));
		root.getTransforms().add(new Rotate(0, Rotate.X_AXIS));
		root.getTransforms().add(new Rotate(0, Rotate.Z_AXIS));

		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				switch (e.getCode()) {
					case P:
						pause = !pause;
						break;
					case W:
						c.setTranslateZ(c.getTranslateZ() + 10);
						break;
					case A:
						c.setTranslateX(c.getTranslateX() - 10);
						break;
					case S:
						c.setTranslateZ(c.getTranslateZ() - 10);
						break;
					case D:
						c.setTranslateX(c.getTranslateX() + 10);
						break;
					case SPACE:
						c.setTranslateY(c.getTranslateY() - 10);
						break;
					case C:
						c.setTranslateY(c.getTranslateY() + 10);
						break;
					default:	
				}
				
			}
		});
		
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				
				if (x < e.getX()) {
					g1.getTransforms().add(new Rotate(.5, Rotate.Y_AXIS));
				}
				if (x > e.getX()) {
					g1.getTransforms().add(new Rotate(-.5, Rotate.Y_AXIS));
				}
				if (y < e.getY()) {
					g1.getTransforms().add(new Rotate(.5, Rotate.X_AXIS));
				}
				if (y > e.getY()) {
					g1.getTransforms().add(new Rotate(-.5, Rotate.X_AXIS));
				}
				
				x = e.getX();
				y = e.getY();
				
			}
		});
		
		primaryStage.setTitle("3SeitigePyramideFX3D");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private Group buildGraphics() {
	    Image img = new Image(Main.class.getResourceAsStream("Bilder/Pyramide.png"));

	    PhongMaterial material = new PhongMaterial();
	    material.setDiffuseMap(img);
	    material.setDiffuseColor(Color.BLUE);

	    float hw = 200;
	    float hh = 200;
	    float hd = 200;

	    float points[] = {
	    		0, -hh, 0,		// 0 Spitze
	    		-hw, 0, -hd,	// 1 Vorne links
	    		hw, 0, -hd,		// 2 Vorne rechts
	    		0, 0, hd		// 3 Hinten
	    };
	    
	    float tex[] = {
	    		0.25375f, 0.41f,	// 0
	    		0.12875f, 0.7f,		// 1
	    		0.38f, 0.7f,		// 2
	    		0f, 0.9967f,		// 3
	    		0.25375f, 0.9967f,	// 4
	    		0.51f, 0.9967f		// 5
	    };

	    int faces[] = {
	    		0,0, 1,1, 2,2,
	    		0,1, 3,3, 1,4,
	    		0,1, 2,4, 3,2,
	    		3,2, 2,5, 1,4
	    };

	    TriangleMesh mesh = new TriangleMesh();
	    mesh.getPoints().addAll(points);
	    mesh.getTexCoords().addAll(tex);
	    mesh.getFaces().addAll(faces);

	    this.pyramid = new MeshView(mesh);
	    this.pyramid.setMaterial(material);
	    
	    return new Group(this.pyramid);
	}
	
	  private SubScene createSubScene(Group group, double width, double height) {
		    SubScene s = new SubScene(group, width, height, true, SceneAntialiasing.BALANCED);
		    this.c = new PerspectiveCamera();
		    c.setTranslateZ(500);
		    s.setCamera(c);
		    s.setFill(Color.color(.1, .1, .1));
		    return s;
	  }
	
	private void draw() {
		if (!this.pause) {
			this.pyramid.getTransforms().add(new Rotate(1, Rotate.X_AXIS));
			this.pyramid.getTransforms().add(new Rotate(1, Rotate.Y_AXIS));
			this.pyramid.getTransforms().add(new Rotate(1, Rotate.Z_AXIS));
		}
	}
}