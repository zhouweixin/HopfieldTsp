package com.zhou;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import com.google.common.primitives.Ints;

public class Main {
	
	private int cityNum = 48;

	private double u0 = 0.02;

	private int[] cityx = { 6734, 2233, 5530, 401, 3082, 7608, 7573, 7265, 6898, 1112, 5468, 5989, 4706, 4612, 6347,
			6107, 7611, 7462, 7732, 5900, 4483, 6101, 5199, 1633, 4307, 675, 7555, 7541, 3177, 7352, 7545, 3245, 6426,
			4608, 23, 7248, 7762, 7392, 3484, 6271, 4985, 1916, 7280, 7509, 10, 6807, 5185, 3023 };
	private int[] cityy = { 1453, 10, 1424, 841, 1644, 4458, 3716, 1268, 1885, 2049, 2606, 2873, 2674, 2035, 2683, 669,
			5184, 3590, 4723, 3561, 3369, 1110, 2182, 2809, 2322, 1006, 4819, 3981, 756, 4506, 2801, 3305, 3173, 1198,
			2216, 3779, 4595, 2244, 2829, 2135, 140, 1569, 4899, 3239, 2676, 2993, 3258, 1942 };

	private Double[] normX = { 0.867389060888, 0.286764705882, 0.712074303406, 0.0504385964912, 0.396284829721,
			0.980134158927, 0.975619195046, 0.9358875129, 0.888544891641, 0.142156862745, 0.704076367389,
			0.771284829721, 0.605779153767, 0.593653250774, 0.817466460268, 0.786506707946, 0.980521155831,
			0.961300309598, 0.99613003096, 0.759803921569, 0.577012383901, 0.785732714138, 0.669375644995,
			0.209365325077, 0.554308565531, 0.0857843137255, 0.973297213622, 0.97149122807, 0.408539731682,
			0.947110423117, 0.972007223942, 0.417311661507, 0.827657378741, 0.593137254902, 0.00167698658411,
			0.933694530444, 1.0, 0.952270381837, 0.448142414861, 0.8076625387, 0.641769865841, 0.245872033024,
			0.93782249742, 0.967363261094, 0.0, 0.876805985552, 0.667569659443, 0.388673890609 };
	private Double[] normY = { 0.278894472362, 0.0, 0.273289524546, 0.160610746038, 0.315809818322, 0.859683030537,
			0.716273676073, 0.243138770777, 0.362388867414, 0.394085813684, 0.501739466564, 0.553343641283,
			0.514882102822, 0.391379976807, 0.516621569385, 0.127367607267, 1.0, 0.691921144182, 0.910900657132,
			0.686316196366, 0.649207576343, 0.212601468883, 0.419791264012, 0.540974101276, 0.446849632779,
			0.19250096637, 0.929454967143, 0.767491302667, 0.144182450715, 0.868960185543, 0.539427908775,
			0.636838036336, 0.61132586007, 0.229609586394, 0.426362582141, 0.728449942018, 0.886161577116,
			0.431774255895, 0.544839582528, 0.410707383069, 0.0251256281407, 0.301314263626, 0.944916892153,
			0.624081948203, 0.515268650947, 0.576536528798, 0.627754155392, 0.373405488983 };

	public static void main(String[] args) {
		Main m = new Main();

		if (args.length > 0) {
			m.cityNum = Integer.parseInt(args[0]);
			m.HOP_TSP(m.cityNum);
		} else {
			m.cityNum = 48;
			m.HOP_TSP(48);
		}
	}
	
	/**
	 * 预处理:归一化
	 */
	@Test
	public void preprocess() {
		int minX = Ints.asList(cityx).stream().min((x, y) -> {return x-y;}).get();
		int maxX = Ints.asList(cityx).stream().max((x, y) -> {return x-y;}).get();
		int minY = Ints.asList(cityy).stream().min((x, y) -> {return x-y;}).get();
		int maxY = Ints.asList(cityx).stream().max((x, y) -> {return x-y;}).get();
		
		normX = Ints.asList(cityx).stream().map(x -> 1.0 * (x-minX) / (maxX-minX)).collect(Collectors.toList()).toArray(new Double[1]);
		normY = Ints.asList(cityy).stream().map(x -> 1.0 * (x-minY) / (maxY-minY)).collect(Collectors.toList()).toArray(new Double[1]);
		
		Arrays.asList(normX).stream().forEach(System.out::println);
	}

	public double computeLen(int[] router) {
		double dis = 0;
		for (int i = 0; i < router.length; i++) {
			int x = router[i % router.length];
			int y = router[(i + 1) % router.length];
			dis += computeDis(cityx[x], cityy[x], cityx[y], cityy[y]);
		}
		return dis;
	}

	public double computeDis(double x1, double y1, double x2, double y2) {
		return Math.sqrt(((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) / 10);
	}

	public double G(double x) {
		// return (1.0 + Math.tanh(x / u0)) / 2.0;
		// TODO 我感觉正确的公式是这样的
		return 1 / (1 + Math.exp(-2 * x / u0));
	}

	/*
	 * 函数介绍：城市的初始化设置， resize(Num)根据输入的城市数设置城市数组， SetCityIndex(i)依次设置某个城市的索引ID，
	 * SetCoordx随机取值，设置某城市在X-Y轴上的X坐标 输入参数：Num,设置城市结点的个数 返回值 ：返回一个初始化好的城市数组
	 */

	// 通过随机函数得到(0,1)之间的城市坐标
	public CityInfo[] CreateCities(int Num) {
		CityInfo[] City = new CityInfo[Num];

		// for (int i = 0; i < Num; i++) {
		// City[i] = new CityInfo();
		// City[i].SetCityIndex(i);// 设置城市索引，也即是第i个城市
		// double t = Math.random();// 得到(0,1)之间的一个数
		// City[i].SetCoordx(t);// 设置城市的x坐标
		// t = Math.random();
		// City[i].SetCoordy(t);// 设置城市的y坐标
		// }

		for (int i = 0; i < Num; i++) {
			City[i] = new CityInfo();
			City[i].setCityIndex(i);
			City[i].setCoordx(normX[i]);
			City[i].setCoordy(normY[i]);
		}

		return City;
	}

	/**
	 * 初始化偏置矩阵
	 * 
	 * @param citys
	 * @return
	 */
	public List<Double> setBias(CityInfo[] citys) {
		List<Double> bias = new ArrayList<Double>();
		double d1 = 0.0, d2 = 0.0, d3 = 0.0, d4 = 0.0;
		// 遍历所有城市
		for (int i = 0; i < citys.length; i++) {
			d1 = (citys[i].getCoordy() - 0.5) / (citys[i].getCoordx() - 0.5);
			d2 = Math.atan(d1); // 反正切函数
			d3 = Math.hypot(citys[i].getCoordx() - 0.5, citys[i].getCoordy() - 0.5);// 平方根
			for (int j = 0; j < citys.length; j++) {
				d4 = d2 + (j - 1) * 2 * Math.PI * d3 / citys.length;
				bias.add(Math.cos(d4));// 往偏置向量中插入此次计算出来的偏置值
			}
		}
		return bias;// 返回偏置向量
	}

	/**
	 * 计算能量
	 * 
	 * @param city
	 * @param u
	 * @param v
	 * @param A
	 * @param B
	 * @param C
	 * @param D
	 * @return
	 */
	double computeEnergy(CityInfo[] city, double[] u, double[] v, double A, double B, double C, double D) {
		double delt = 0, E = 0, k = 0, h = 0.01;
		double J1 = 0.0, J2 = 0.0, J3 = 0.0, J = 0.0;
		int Num = city.length;// 获得城市的数目
		/// 计算J1,也是约束条件，即在换位矩阵中，每一城市行x 至多含有一个“1”，其余都是“0”，
		for (int x = 0; x < Num; x++) {
			for (int i = 0; i < Num - 1; i++) {
				// j从i+1开始是为了避免j=i的情况
				for (int j = i + 1; j < Num; j++) {
					J1 += v[x * Num + i] * v[x * Num + j];// J1+=v[x][i]*v[x][j]
				}
			}
		}
		// 计算J2,也是约束条件，即在置换矩阵中，每一城市列y 至多含有一个“1”，其余都是“0”，
		for (int i = 0; i < Num; i++)
			for (int x = 0; x < Num - 1; x++)
				// y从x+1开始是为了避免y=x的情况
				for (int y = x + 1; y < Num; y++)
					J2 += v[x * Num + i] * v[y * Num + i];// j2+=v[x][i]*v[y][i]
		// 计算J3,其中K 是计算置换矩阵的总和；J3也是约束条件，即在置换矩阵中,只能有N个1；最后一步平方，是为了防止出现负数
		for (int x = 0; x < Num; x++)
			for (int i = 0; i < Num; i++)
				k += v[x * Num + i]; // 计算能量系数,k+=v[x][i]
		J3 = (k - Num) * (k - Num);
		/*
		 * 计算J,可行旅行路线的路程 J=min(sum(d[x][y]*v[x][i]*(v[y][i+1]+v[y][i-1]))),y!=x;
		 * v[x][i]的行下标x是城市编号，列下标i表示城市x在旅行顺序中的位置，下标对N取模运算
		 */
		for (int x = 0; x < Num; x++) {
			for (int y = 0; y < Num; y++) {
				for (int i = 0; i < Num; i++) {
					if (i == 0)
						// 下标对N取模运算,由于i-1<0,而i从0开始取值,所以取模后i为Num-1
						J += city[x].getCityDis(city[y]) * v[x * Num + i] * (v[y * Num + Num - 1] + v[y * Num + i + 1]); // J+=dis[x][y]*v[x][i]*(v[y][N-1]+v[y][i+1]
					else if (i == Num - 1)
						J += city[x].getCityDis(city[y]) * v[x * Num + i] * (v[y * Num + i - 1] + v[y * Num]);// J+=dis[x][y]*v[x][i]*(v[y][i-1]+v[y][0])
					else
						J += city[x].getCityDis(city[y]) * v[x * Num + i] * (v[y * Num + i - 1] + v[y * Num + i + 1]);
				}
			}
		}
		// 得到能量函数
		E = A * J1 / 2 + B * J2 / 2 + C * J3 / 2 + D * J / 2;

		/*
		 * 取神经元的I/O函数为S型函数，可以求得TSP问题的网络方程
		 * delt=-u[x][i]-A*Sum(v[x][j])-B*Sum(v[y][j]-C*(Sum(v[x][i])-N)-D*Sum(d[x][y])(
		 * v[y][i+1]+v[y][i-1])); u[x*Num+i]=h*delt; v[x][i]通过G(u[u][i])求得
		 */
		for (int x = 0; x < Num; x++) {
			for (int i = 0; i < Num; i++) {
				delt = 0 - u[x * Num + i];// u[x][i]

				for (int j = 0; j < Num; j++) {
					if (i == j)
						continue;
					delt -= A * v[x * Num + j]; // v[x][j]
				}

				for (int y = 0; y < Num; y++) {
					if (x == y)
						continue;
					delt -= B * v[y * Num + i]; // v[y][i]
				}

				delt -= C * (k - Num);// k=Sum(v[x][i])
				// i需对N取模
				for (int y = 0; y < Num; y++) {
					if (i == 0)
						delt -= D * city[x].getCityDis(city[y]) * (v[y * Num + Num - 1] + v[y * Num + i + 1]);
					else if (i == Num - 1)
						delt -= D * city[x].getCityDis(city[y]) * (v[y * Num + i - 1] + v[y * Num]);
					else
						delt -= D * city[x].getCityDis(city[y]) * (v[y * Num + i - 1] + v[y * Num + i + 1]);
				}
				u[x * Num + i] += h * delt;// 缩小系数比例
				v[x * Num + i] = G(u[x * Num + i]); // v[x][i]=G(u[x][i])
			}
		}
		return E;
	}

	boolean reValid(int[] router) {
		
		Set<Integer> set = new HashSet<Integer>();
		set.addAll(Ints.asList(router));
		if(set.size() != cityNum) {
			return false;
		}
		
		for (int i = 0; i < router.length; i++) {
			for (int j = i + 1; j < router.length; j++) {
				if (router[i] == router[j] || router[i] < 0 || router[i] > router.length - 1)
					return false;
			}
		}
		return true;
	}

	void PrintCity(CityInfo[] city) {
		for (int i = 0; i < city.length; i++)
			System.out.println(city[i].getCityIndex() + "	" + city[i].getCoordx() + "	" + city[i].getCoordy());
	}

	void HOP_TSP(int cityNum) {
		// 创建城市数目
		CityInfo[] cities = CreateCities(cityNum);

		// 路径向量
		int[] routers = new int[cityNum];

		// 初始偏置向量
		List<Double> list = setBias(cities);
		Double[] initBias = list.toArray(new Double[1]);

		// InSig即是上面的求能量函数中的换位矩阵U向量，OutSig是求能量函数中的换位矩阵V向量
		double[] inSig = new double[cityNum * cityNum];
		double[] outSig = new double[cityNum * cityNum];

		// 路径长度
		double totalDis = 0.0;
		// 能量
		double energy = 0.0;
		// 调节双曲正切函数tanh
		double u0 = 0.02;

		// 最短路径的长度
		double minTotalDis;
		// 最短路径序列
		int[] minRouter = new int[cityNum];// 最小的路径向量

		// 通过for循环改变A,B,C的参数值
//		for (double dd = 0.1; dd < 0.5; dd += 0.05) {
		if(true) {
			double dd = 0.1;
			minTotalDis = 20.0;// TODO

			for (int it = 0; it < 50; it++) {
				int itNum = 0;

				double u00 = 0 - u0 * Math.log(cityNum - 1) / 2;
				for (int i = 0; i < cityNum * cityNum; i++) {
					double t = Math.random();

					// 求初始换位矩阵
					inSig[i] = u00 + 0.001 * (t * 2 - 1) * initBias[i];
					outSig[i] = G(inSig[i]); // G()是罚函数
				}

				double temp = 0;
				while (itNum < 10000) {
					energy = computeEnergy(cities, inSig, outSig, dd, dd, dd, 0.1);// 求能量函数
					if (Math.abs(energy - temp) < 1e-5)
						break;
					temp = energy;
					itNum++;
				}

				int i, j, k = 0;
				int count = 0;

				totalDis = 0.0;// TSP总距离
				// 通过for循环得到换位矩阵中首列出现的城市的编号，即第一个城市的编号，保存在Router[0]中
				for (j = 0; j < cityNum; j++) {
					if (outSig[j * cityNum] >= 0.2) {
						k = j;
						routers[count] = j;// Router用于保存顺次旅行城市的编号
						count += 1;
						break;
					}
				}
				// 通过for循环得到换位矩阵其它列出现的城市编号，保存在Router[i]中，同时得到城市的旅行总距离，保存在TotalDis中
				for (i = 1; i < cityNum; i++) {
					for (j = 0; j < cityNum; j++) {
						if (outSig[j * cityNum + i] >= 0.2) {// 凡置换矩阵中，大于0.2的状态，认为是正确的路径结点
							routers[count] = j;
							count += 1;
							// ::k表示置换矩阵中某一列中正确路径结点的行号，j为K所相邻列中正确路径结点的行号；两个相邻的行号，才能确定两点间的距离
							totalDis += cities[k].getCityDis(cities[j]);
							k = j;
							break;
						}
					}
				}
				totalDis += cities[k].getCityDis(cities[routers[0]]);

				if (reValid(routers)) {
					if (totalDis < minTotalDis) {
						minTotalDis = totalDis;
						minRouter = routers.clone();
					}

					double d = computeLen(routers);

					System.out.println("\r\n");
					System.out.println("A=" + dd + ", B=" + dd + ", C=" + dd + ", D=0.1, epoch=" + itNum);
					System.out.println("能量值: " + energy);
					System.out.println("当前路径长度  : " + d);
					for (i = 0; i < cityNum; i++)
						System.out.print((routers[i]+1) + " ");
				} else {
//					System.out.println("无效路径");// 显示路径非法
				}

			}
		}
		double d = computeLen(minRouter);
		System.out.println("\r\n\r\n============结果==============");
		System.out.println("最短路径长度：" + d);
		System.out.print("最小路径为：");
		for (int ki = 0; ki < cityNum; ki++) {
			System.out.print((minRouter[ki] + 1) + " ");
		}
		System.out.println();
		new MyFrame().showCities(cityx, cityy, minRouter);
	}
}
