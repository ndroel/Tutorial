
import java.text.DecimalFormat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author oyi
 */
public class mobil {

    static double kelas[] = {1, 1, -1, -1, 1};
    static double kelasuji[] = {1, 1, -1};
    static double data[][] = {{4.1, 1.65, 1.7}, {3.9, 1.7, 1.5}, {4.2, 1.7, 1.8}, {4.4, 1.6, 1.55}, {4, 1.8, 1.6}};
    static double datauji[][] = {{4, 1.5, 1.8}, {3.7, 1.9, 1.2}, {3.9, 1.7, 1.4}};
    static double data_ygdicari[] = {4, 1.6, 1.8};
    static int barisuji = datauji.length;
    static int counterdata = data.length * data[0].length;
    static int jumkol = data[0].length;
    static int baris = data.length;
    static double[] matrik_kernel_xmin, alpha, matrik_kernel_xpos;
    static double temp_jumwxmin, temp_jumwxpos, bias;

    private static double max(double[][] data, int j) {
        double max = 0;
        int jumdat = data.length * data[0].length / data[0].length;
        for (int i = 0; i < jumdat; i++) {

            if (data[i][j] > max) {
                max = data[i][j];
            }
        }
        return max;
    }

    private static double min(double[][] data, int j) {
        double min = 1000;
        int jumdat = data.length * data[0].length / data[0].length;
        for (int i = 0; i < jumdat; i++) {
            if (min > data[i][j]) {
                min = data[i][j];
            }
        }
        return min;
    }

    public Object[][] normalisasiminMax(double[][] data) {
        int baris = data.length;
        int kolom = data[0].length;
        Object[][] newdata = new Object[baris][kolom];
        String s;
        int j, i, jumdat = baris * kolom / kolom;
        int newmax = 1;
        int newmin = 0;
        for (j = 0; j < kolom; j++) {
            double max = max(data, j);
            double min = min(data, j);
            for (i = 0; i < jumdat; i++) {
                newdata[i][j] = ((data[i][j] - min) * (newmax - newmin)) / ((max - min) + newmin);
                //newdata[i][j] = (int)(newdata[i][j]*100);
                System.out.print(data[i][j]);
                System.out.println("");
                System.out.print(newdata[i][j]);
            }
        }
        System.out.println("normal");
        DecimalFormat df = new DecimalFormat("#.###");
        for (j = 0; j < data.length; j++) {
            s = "" + df.format(newdata[j][0]) + "\t" + df.format(newdata[j][1]) + "\t" + df.format(newdata[j][2]);
            System.out.println(s);
        }
        return newdata;
    }

    public double[][] carikernel(Object[][] data, int d) {
        System.out.println("kernel");
        DecimalFormat df = new DecimalFormat("#.###");
        double pol = 0;
        double[] jumlah;
        int index = 1;
        double[][] matrik_kernel = new double[data.length][data.length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                pol = 0;
                for (int k = 0; k < data[0].length; k++) {
                    pol = pol + ((double) data[i][k] * (double) data[j][k]);
                }
                matrik_kernel[i][j] = Math.pow(pol, d);
                System.out.print(matrik_kernel[i][j] + " ");
            }
            System.out.println("");
        }
        return matrik_kernel;
    }

    public double[][] carihesian(double[][] data, double lamda, int kelas[]) {
        System.out.println("hesian");
        double[][] matrix_hesian = new double[data.length][data.length];
        for (int i = 0; i < matrix_hesian.length; i++) {
            for (int j = 0; j < matrix_hesian.length; j++) {
                matrix_hesian[i][j] = kelas[i] * kelas[j] * (data[i][j] + Math.pow(lamda, 2));
                System.out.print(matrix_hesian[i][j] + " ");
            }
            System.out.println("");
        }
        return matrix_hesian;
    }

    public double[][] sequensial_training(double[][] data, int itermax, int C, double alpaawal, double epsilon, double konstanta) {
        int iterasi, iterasistop;
        double maxdeltaalpa = 0, tampungdeltaalpha, maxdiagonal = data[0][0], gamma;
        double[][] matrix_ei = new double[data.length][data.length];
        double[] delta_alpa = new double[data.length];
        double[] tampung_matrix_ei = new double[data.length];
        alpha = new double[data.length];
        double hasil[][] = new double[data.length][2];
        for (int i = 0; i < data.length; i++) {
            if (maxdiagonal < data[i][i]) {
                maxdiagonal = data[i][i];
            }
        }
        System.out.println("max diagonal " + maxdiagonal);
        gamma = konstanta / maxdiagonal;
        for (int i = 0; i < data.length; i++) {
            alpha[i] = alpaawal;
        }
        for (iterasi = 0; iterasi < itermax; iterasi++) {
            System.out.println("iterasi ke -" + iterasi);
            for (int i = 0; i < data.length; i++) {
                double temp_alpa = 0;
                for (int j = 0; j < data.length; j++) {
                    matrix_ei[i][j] = data[i][j] * alpha[j];
                    temp_alpa += matrix_ei[i][j];
                }
                tampung_matrix_ei[i] = temp_alpa;
                tampungdeltaalpha = Math.min((Math.max(gamma * (1 - tampung_matrix_ei[i]),
                        -1 * alpha[i])), C - alpha[i]);
                delta_alpa[i] = tampungdeltaalpha;
                maxdeltaalpa = delta_alpa[0];
            }//for i
            for (int a = 0; a < data.length; a++) {
                alpha[a] += delta_alpa[a];
            }//for a
            //max delta alpha
            for (int i = 0; i < data.length; i++) {
                if (delta_alpa[i] > maxdeltaalpa) {
                    maxdeltaalpa = delta_alpa[i];
                }//if
            }//i
            //fungsi iterasi
            System.out.println("ei");
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data.length; j++) {
                    System.out.print(matrix_ei[i][j] + " ");
                }
                System.out.println("");
            }
            System.out.println("nilai ei \t alpha");
            for (int i = 0; i < data.length; i++) {
                hasil[i][0] = tampung_matrix_ei[i];
                hasil[i][1] = alpha[i];
                System.out.printf("%.10f \t %.10f \n", tampung_matrix_ei[i], alpha[i]);
            }
            System.out.println("max delta " + maxdeltaalpa);
            System.out.println("epsilon " + epsilon);
            if (maxdeltaalpa < epsilon) {
                iterasistop = iterasi;
                break;
            }//if
        }//iterasi
        return hasil;
    }
    static int xmin, xpos;

    public int[] carixminxpos(double[][] data, int kelas[]) {
        int index, index1;
        int xminxpos[] = new int[2];
        int indexpos[] = new int[data.length];
        int indexmin[] = new int[data.length];
        xmin = xpos = 0;
        double datamax = data[0][1];
        index = index1 = 0;
        for (int i = 0; i < data.length; i++) {
            if (kelas[i] > 0) {
                indexpos[index] = i;
                index++;
            } else {
                indexmin[index1] = i;
                index1++;
            }
        }
        //System.out.println("max");
        for (int i = 0; i < index; i++) {
            if (data[indexpos[i]][1] > datamax) {
                datamax = data[indexpos[i]][1];
                xpos = indexpos[i];
            }
        }

        double datamin = data[0][1];
        for (int i = 0; i < index1; i++) {
            if (data[indexmin[i]][1] < datamin) {
                //System.out.println(data[indexpos[i]][1]+"<"+datamin);
                datamin = data[indexmin[i]][1];
                xmin = indexmin[i];
            }
        }
        xminxpos[0] = xmin;
        xminxpos[1] = xpos;
        return xminxpos;
    }

    public double[][] kernelxminxpos(Object data[][], int d, int xminxpos[]) {
        double pol, pol1;
        double[][] kernelxminxpos = new double[data.length][2];
        matrik_kernel_xmin = new double[data.length];
        matrik_kernel_xpos = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            pol = pol1 = 0;
            for (int k = 0; k < jumkol; k++) {
                pol = pol + ((double) data[i][k] * (double) data[xminxpos[0]][k]);
                pol1 = pol1 + ((double) data[i][k] * (double) data[xminxpos[1]][k]);
            }
            matrik_kernel_xmin[i] = Math.pow(pol, d);
            matrik_kernel_xpos[i] = Math.pow(pol1, d);
        }
        System.out.println("kernel xmin\t xpos");
        for (int i = 0; i < matrik_kernel_xmin.length; i++) {
            kernelxminxpos[i][0] = matrik_kernel_xpos[i];
            kernelxminxpos[i][1] = matrik_kernel_xmin[i];
            System.out.println(matrik_kernel_xmin[i] + "\t" + matrik_kernel_xpos[i]);
        }
        return kernelxminxpos;
    }

    public double[][] hitungwx(double kernelxminxpos[][], int kelas[], double ei_alpa[][]) {
        double wx[][] = new double[ei_alpa.length + 1][2];
        double wxpos1[] = new double[ei_alpa.length];
        double wxneg1[] = new double[ei_alpa.length];
        temp_jumwxpos = 0;
        temp_jumwxmin = 0;
        for (int i = 0; i < ei_alpa.length; i++) {
            wxpos1[i] = kernelxminxpos[i][0] * kelas[i] * ei_alpa[i][1];
            temp_jumwxpos += wxpos1[i];
            System.out.println("wxpos : " + wxpos1[i]);
        }//for
        for (int i = 0; i < ei_alpa.length; i++) {
            wxneg1[i] = kernelxminxpos[i][1] * kelas[i] * ei_alpa[i][1];
            temp_jumwxmin += wxneg1[i];
            System.out.println("Nilai WX neg :" + wxneg1[i]);
        }
        int index = 0;
        for (int i = 0; i < wxneg1.length; i++) {
            wx[i][0] = wxpos1[i];
            wx[i][1] = wxneg1[i];
            index = i;
        }
        System.out.println(index);
        wx[index + 1][0] = temp_jumwxpos;
        wx[index + 1][1] = temp_jumwxmin;
        System.out.println(wx[index + 1][0]);
        System.out.println(wx[index + 1][1]);
        return wx;
    }//wxpos

    public double nilaibias(double[][] wxminwxpos) {
        bias = -(wxminwxpos[wxminwxpos.length - 1][0] + wxminwxpos[wxminwxpos.length - 1][0]) / 2;
        System.out.println("bias" + bias);
        return bias;
    }

    public int[] hitungfx(Object latih[][], Object uji[][], double d, int kelas[], double ei_alpa[][], double bias) {
        double kelashasil;
        double temp[] = new double[latih.length];
        double kerneluji[] = new double[latih.length];
        double bobotuji[] = new double[latih.length];
        double totbobot = 0, sign;
        double hasilbobotuji[] = new double[uji.length];
        int hasil[] = new int[uji.length];
        for (int i = 0; i < uji.length; i++) { 
            for (int j = 0; j < latih.length; j++) {
                 temp[j] = 0;
                for (int k = 0; k < latih[0].length; k++) {
                    temp[j] +=((double)uji[i][k] * (double)latih[j][k]);
                    System.out.println("uji\t latih \t\t perkalian \t temp");
                    System.out.printf("%.4f\t %.4f \t %.4f \t %.4f\n",(double)uji[i][k],(double)latih[j][k],((double)uji[i][k] * (double)latih[j][k]),temp[j]);
                }
                System.out.println("--------------");
                kerneluji[j] = Math.pow(temp[j], d);
                
                bobotuji[j] = kerneluji[j] * kelas[i]* ei_alpa[j][1];
                System.out.println("bobot "+bobotuji[j]);
                totbobot += bobotuji[j];
            }

            System.out.println("total bobot "+totbobot);
            hasilbobotuji[i] = totbobot;
            sign = hasilbobotuji[i] + bias;
            System.out.println(sign);
            if (sign > 0) {
                hasil[i]= 1;
            }else{
                hasil[i]=-1;
            }
            System.out.println("hasil : data ke "+i+" = "+hasil[i]);
            //   matrik_kernel_xmin[i] = Math.pow(temp, 2);
        }
        return hasil;
    }

}
