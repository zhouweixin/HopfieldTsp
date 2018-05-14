package com.zhou;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import com.google.common.primitives.Ints;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MyFrame extends Application {

	int xSize = 1000;
	int ySize = 600;

	int[] cityx = { 6734, 2233, 5530, 401, 3082, 7608, 7573, 7265, 6898, 1112, 5468, 5989, 4706, 4612, 6347, 6107, 7611,
			7462, 7732, 5900, 4483, 6101, 5199, 1633, 4307, 675, 7555, 7541, 3177, 7352, 7545, 3245, 6426, 4608, 23,
			7248, 7762, 7392, 3484, 6271, 4985, 1916, 7280, 7509, 10, 6807, 5185, 3023 };
	int[] cityy = { 1453, 10, 1424, 841, 1644, 4458, 3716, 1268, 1885, 2049, 2606, 2873, 2674, 2035, 2683, 669, 5184,
			3590, 4723, 3561, 3369, 1110, 2182, 2809, 2322, 1006, 4819, 3981, 756, 4506, 2801, 3305, 3173, 1198, 2216,
			3779, 4595, 2244, 2829, 2135, 140, 1569, 4899, 3239, 2676, 2993, 3258, 1942 };

	static Double[] cx = null;
	static Double[] cy = null;
	static int[] routers = { 19, 32, 17, 6, 5, 36, 18, 26, 16, 42, 29, 27, 35, 43, 30, 45, 11, 14, 37, 39, 8, 7, 0, 21,
			15, 40, 2, 10, 22, 12, 13, 24, 33, 47, 4, 28, 1, 41, 25, 3, 34, 44, 9, 23, 31, 38, 20, 46 };

	// static int[] routers = { 19, 42, 16, 26, 18, 36, 5, 27, 6, 17, 29, 43, 35,
	// 30, 45, 14, 37, 12, 10, 11, 32, 2, 39, 8,
	// 7, 0, 21, 15, 40, 33, 28, 1, 25, 3, 34, 44, 9, 23, 4, 47, 31, 38, 24, 13, 41,
	// 22, 20, 46 };

	public static void main(String[] args) {
		MyFrame testFrame = new MyFrame();
		testFrame.showCities(testFrame.cityx, testFrame.cityy, MyFrame.routers);
	}

	// 画路线
	public void showCities(int[] cityx, int[] cityy, int[] routers) {

		// for (int ki = 0; ki < routers.length; ki++) {
		// System.out.print(routers[ki] + " ");
		// }
		// System.out.println();

		MyFrame.routers = routers;

		int maxX = Ints.asList(cityx).stream().max((x, y) -> {
			return x - y;
		}).get();
		int maxY = Ints.asList(cityy).stream().max((x, y) -> {
			return x - y;
		}).get();
		cx = Ints.asList(cityx).stream().map(x -> 10 + 1.0 * (xSize - 40) * x / maxX).collect(Collectors.toList())
				.toArray(new Double[1]);
		cy = Ints.asList(cityy).stream().map(x -> 10 + 1.0 * (ySize - 40) * x / maxY).collect(Collectors.toList())
				.toArray(new Double[1]);
		Application.launch(MyFrame.class);
	}

	@Override
	public void start(Stage primaryStage) {
		Canvas canvas = new Canvas(xSize, ySize);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		drawShapes(gc);

		Group root = new Group();
		root.getChildren().add(canvas);

		computeLen();
		primaryStage.setTitle("Hopfield TSP, 长度:" + (int) computeLen());
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	private void drawShapes(GraphicsContext gc) {

		gc.setFill(Color.RED);
		for (int i = 0; i < cx.length; i++) {
			gc.fillOval(cx[i] - 5, cy[i] - 5, 10, 10);
			gc.fillText((i + 1) + "", cx[i] - 5, cy[i] + 15);
		}

		if (routers != null) {
			gc.setStroke(Color.BLUE);

			for (int i = 0; i < routers.length; i++) {
				int x = routers[i];
				int y = routers[(i + 1) % routers.length];

				gc.strokeLine(cx[x], cy[x], cx[y], cy[y]);
			}
		}
	}

	@Test
	public void test() {
		routers = new int[] { 19, 32, 17, 6, 5, 36, 18, 26, 16, 42, 29, 27, 35, 43, 30, 45, 11, 14, 37, 39, 8, 7, 0, 21,
				15, 40, 2, 10, 22, 12, 13, 24, 33, 47, 4, 28, 1, 41, 25, 3, 34, 44, 9, 23, 31, 38, 20, 46 };
		Set<Integer> set = new HashSet<Integer>();
		set.addAll(Ints.asList(routers));

		System.out.println(routers.length);
		System.out.println(set.size());

		System.out.println();
		System.out.println(computeLen());
	}

	public double computeLen() {

		double dis = 0;
		for (int i = 0; i < routers.length; i++) {
			int x = routers[i % routers.length];
			int y = routers[(i + 1) % routers.length];
			dis += computeDis(cityx[x], cityy[x], cityx[y], cityy[y]);
		}
		return dis;
	}

	/**
	 * 计算伪欧式距离
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public double computeDis(int x1, int y1, int x2, int y2) {
		return Math.hypot(x1 - x2, y1 - y2) / Math.sqrt(10);
	}
}
