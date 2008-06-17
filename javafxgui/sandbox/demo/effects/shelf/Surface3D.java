/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package demo.effects.shelf;

/**
 * Surface3D
 *
 * @author Jim Graham
 */
public class Surface3D {
    double matrix[][];

    static Surface3D defaultCamera;

    static {
        defaultCamera = new Surface3D();
        defaultCamera.concat(1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 0, 0,
                0, 0, 1, 0);
        //System.out.println("default camera = " + defaultCamera);
    }

    public Surface3D() {
        matrix = new double[4][4];
        matrix[0][0] = matrix[1][1] = matrix[2][2] = matrix[3][3] = 1.0;
    }

    public Surface3D(Surface3D other) {
        this.matrix = new double[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(other.matrix[i], 0, this.matrix[i], 0, 4);
        }
    }

//   public PerspectiveTransform getPerspectiveTransform() {
//       return getPerspectiveTransform(defaultCamera);
//   }

//   public PerspectiveTransform getPerspectiveTransform(Surface3D c) {
//       Point3D ul = new Point3D(0, 0, 0);
//       Point3D ur = new Point3D(1, 0, 0);
//       Point3D ll = new Point3D(0, 1, 0);
//       Point3D lr = new Point3D(1, 1, 0);
//       transform(ul);
//       transform(ur);
//       transform(ll);
//       transform(lr);
//       if (false) {
//           System.out.println("ul = "+ul);
//           System.out.println("ur = "+ur);
//           System.out.println("ll = "+ll);
//           System.out.println("lr = "+lr);
//       }
//       c.transform(ul);
//       c.transform(ur);
//       c.transform(ll);
//       c.transform(lr);
//       if (false) {
//           System.out.println("ul = "+ul);
//           System.out.println("ur = "+ur);
//           System.out.println("ll = "+ll);
//           System.out.println("lr = "+lr);
//       }
//       return PerspectiveTransform.getSquareToQuad(ul.getX(), ul.getY(),
//                                                   ur.getX(), ur.getY(),
//                                                   lr.getX(), lr.getY(),
//                                                   ll.getX(), ll.getY());
//   }

    public Surface3D createInverse() {
        Surface3D s3d = new Surface3D();
        double det = getDeterminant();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                s3d.matrix[row][col] = cofactor(col, row) / det;
            }
        }
        return s3d;
    }

    public void transform(Point3D p) {
        double x = p.x;
        double y = p.y;
        double z = p.z;
        double w = p.w;
        p.x = dot(matrix[0], x, y, z, w);
        p.y = dot(matrix[1], x, y, z, w);
        p.z = dot(matrix[2], x, y, z, w);
        p.w = dot(matrix[3], x, y, z, w);
    }

    public void inverseTransform(Point3D p) {
        double x = p.x;
        double y = p.y;
        double z = p.z;
        double w = p.w;

        p.x = (cofactor(0, 0) * x +
                cofactor(1, 0) * y +
                cofactor(2, 0) * z +
                cofactor(3, 0) * w);
        p.y = (cofactor(0, 1) * x +
                cofactor(1, 1) * y +
                cofactor(2, 1) * z +
                cofactor(3, 1) * w);
        p.z = (cofactor(0, 2) * x +
                cofactor(1, 2) * y +
                cofactor(2, 2) * z +
                cofactor(3, 2) * w);
        p.w = (cofactor(0, 3) * x +
                cofactor(1, 3) * y +
                cofactor(2, 3) * z +
                cofactor(3, 3) * w);
    }

    public double getDeterminant() {
        return (+matrix[0][0] * minor(0, 0)
                - matrix[0][1] * minor(0, 1)
                + matrix[0][2] * minor(0, 2)
                - matrix[0][3] * minor(0, 3));
    }

    double cofactor(int row, int col) {
        return minor(row, col) * (1.0 - ((row + col) * 2.0));
    }

    double minor(int row, int col) {
        int r1 = (row + 1) % 4;
        int r2 = (row + 2) % 4;
        int r3 = (row + 3) % 4;
        int c1 = (col + 1) % 4;
        int c2 = (col + 2) % 4;
        int c3 = (col + 3) % 4;
        double m[][] = matrix;
        return (m[r1][c1] * (m[r2][c2] * m[r3][c3] - m[r2][c3] * m[r3][c2]) +
                m[r1][c2] * (m[r2][c3] * m[r3][c1] - m[r2][c1] * m[r3][c3]) +
                m[r1][c3] * (m[r2][c1] * m[r3][c2] - m[r2][c2] * m[r3][c1]));
    }

    public double Tx(double x, double y, double z) {
        return dot(matrix[0], x, y, z, 1) / dot(matrix[3], x, y, z, 1);
    }

    public double Ty(double x, double y, double z) {
        return dot(matrix[1], x, y, z, 1) / dot(matrix[3], x, y, z, 1);
    }

    public double Tz(double x, double y, double z) {
        return dot(matrix[2], x, y, z, 1) / dot(matrix[3], x, y, z, 1);
    }

    double dot(double row[], double x, double y, double z, double w) {
        return (x * row[0] + y * row[1] + z * row[2] + w * row[3]);
    }

    public void concatenate(Surface3D s3d) {
        double result[][] = new double[4][4];
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                for (int i = 0; i < 4; i++) {
                    result[r][c] += this.matrix[r][i] * s3d.matrix[i][c];
                }
            }
        }
        this.matrix = result;
    }

    public void preConcatenate(Surface3D s3d) {
        double result[][] = new double[4][4];
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                for (int i = 0; i < 4; i++) {
                    result[r][c] += s3d.matrix[r][i] * this.matrix[i][c];
                }
            }
        }
        this.matrix = result;
    }

    public void concat(double b00, double b01, double b02, double b03,
                       double b10, double b11, double b12, double b13,
                       double b20, double b21, double b22, double b23,
                       double b30, double b31, double b32, double b33) {
        matrix[0] = new double[]{
                dot(matrix[0], b00, b10, b20, b30),
                dot(matrix[0], b01, b11, b21, b31),
                dot(matrix[0], b02, b12, b22, b32),
                dot(matrix[0], b03, b13, b23, b33),
        };

        matrix[1] = new double[]{
                dot(matrix[1], b00, b10, b20, b30),
                dot(matrix[1], b01, b11, b21, b31),
                dot(matrix[1], b02, b12, b22, b32),
                dot(matrix[1], b03, b13, b23, b33),
        };

        matrix[2] = new double[]{
                dot(matrix[2], b00, b10, b20, b30),
                dot(matrix[2], b01, b11, b21, b31),
                dot(matrix[2], b02, b12, b22, b32),
                dot(matrix[2], b03, b13, b23, b33),
        };

        matrix[3] = new double[]{
                dot(matrix[3], b00, b10, b20, b30),
                dot(matrix[3], b01, b11, b21, b31),
                dot(matrix[3], b02, b12, b22, b32),
                dot(matrix[3], b03, b13, b23, b33),
        };
    }

    public void rotateAroundZ(double theta) {
        double sin = Math.sin(theta);
        double cos = Math.cos(theta);
        concat(cos, -sin, 0, 0,
                sin, cos, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);
    }

    public void rotateAroundY(double theta) {
        double sin = Math.sin(theta);
        double cos = Math.cos(theta);
        concat(cos, 0, sin, 0,
                0, 1, 0, 0,
                -sin, 0, cos, 0,
                0, 0, 0, 1);
    }

    public void rotateAroundX(double theta) {
        double sin = Math.sin(theta);
        double cos = Math.cos(theta);
        concat(1, 0, 0, 0,
                0, cos, -sin, 0,
                0, sin, cos, 0,
                0, 0, 0, 1);
    }

    public void translate(double tx, double ty, double tz) {
        concat(1, 0, 0, tx,
                0, 1, 0, ty,
                0, 0, 1, tz,
                0, 0, 0, 1);
    }

    public void scale(double sx, double sy, double sz) {
        concat(sx, 0, 0, 0,
                0, sy, 0, 0,
                0, 0, sz, 0,
                0, 0, 0, 1);
    }

    public void shearX(double shy, double shz) {
        concat(1, shy, shz, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);
    }

    public void shearY(double shx, double shz) {
        concat(1, 0, 0, 0,
                shx, 1, shz, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);
    }

    public void shearZ(double shx, double shy) {
        concat(1, 0, 0, 0,
                0, 1, 0, 0,
                shx, shy, 1, 0,
                0, 0, 0, 1);
    }

    public void rotateXYaroundZ(double x, double y, double theta) {
        translate(x, y, 0);
        rotateAroundZ(theta);
        translate(-x, -y, 0);
    }

    public void rotateXZaroundY(double x, double z, double theta) {
        translate(x, 0, z);
        rotateAroundY(theta);
        translate(-x, 0, -z);
    }

    public void rotateYZaroundX(double y, double z, double theta) {
        translate(0, y, z);
        rotateAroundX(theta);
        translate(0, -y, -z);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("3D Surface transform matrix\n");
        append(matrix[0], sb);
        append(matrix[1], sb);
        append(matrix[2], sb);
        append(matrix[3], sb);
        return new String(sb);
    }

    void append(double row[], StringBuffer sb) {
        sb.append(row[0]);
        sb.append("\t");
        sb.append(row[1]);
        sb.append("\t");
        sb.append(row[2]);
        sb.append("\t");
        sb.append(row[3]);
        sb.append("\n");
    }

    public static class Point3D {
        double x;
        double y;
        double z;
        double w;

        public Point3D() {}

        public Point3D(Point3D p) {
            this(p.x, p.y, p.z, p.w);
        }

        public Point3D(double x, double y, double z) {
            this(x, y, z, 1);
        }

        public Point3D(double x, double y, double z, double w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

        public double getX() {
            return x / w;
        }

        public double getY() {
            return y / w;
        }

        public double getZ() {
            return z / w;
        }

        public String toString() {
            return "Surface3D(" + x + ", " + y + ", " + z + ", " + w + ")";
        }
    }
}
