package ru.cpb9.geotarget.ui;

import com.sun.javafx.geom.Vec3f;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Renderable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

/**
 * @author Artem Shein
 */
public class ColoredSphere implements Renderable
{
    @NotNull
    private final Vec3f color;
    @Nullable
    private Position position;
    private double radius;

    public static ColoredSphere newInstance(@Nullable Position position, double radius, @NotNull Vec3f color)
    {
        return new ColoredSphere(position, radius, color);
    }

    @Override
    public void render(DrawContext drawContext)
    {
        if (position == null)
        {
            return;
        }
        Vec4 point = drawContext.getTerrain().getSurfacePoint(position);
        GL2 gl = drawContext.getGL().getGL2();
        gl.glPushAttrib(GL2.GL_CURRENT_BIT);
        gl.glColor3f(color.x, color.y, color.z);
        GLU glu = drawContext.getGLU();
        gl.glPushMatrix();
        gl.glTranslated(point.x, point.y, point.z);
        GLUquadric quadric = glu.gluNewQuadric();
        glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
        glu.gluQuadricNormals(quadric, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(quadric, GLU.GLU_OUTSIDE);
        glu.gluSphere(quadric, radius, 128, 128);
        gl.glPopMatrix();
        glu.gluDeleteQuadric(quadric);
        gl.glPopAttrib();
    }

    private ColoredSphere(@Nullable Position position, double radius, @NotNull Vec3f color)
    {
        this.radius = radius;
        this.position = position;
        this.color = color;
    }

    public void setPosition(@NotNull Position position)
    {
        this.position = position;
    }
}
