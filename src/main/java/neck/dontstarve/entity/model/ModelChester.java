package neck.dontstarve.entity.model;

import neck.dontstarve.entity.EntityChester;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.util.math.MathHelper;

/**
 * chester - neck
 * Created using Tabula 7.0.0
 */
public class ModelChester extends ModelBase {
    public ModelRenderer body;
    public ModelRenderer leg_front_left_1;
    public ModelRenderer leg_front_right_1;
    public ModelRenderer leg_back_left_1;
    public ModelRenderer leg_back_right_1;
    public ModelRenderer mouth_back;
    public ModelRenderer tooth_lower_2;
    public ModelRenderer tooth_lower_4;
    public ModelRenderer tooth_lower_5;
    public ModelRenderer tooth_lower_6;
    public ModelRenderer tooth_lower_8;
    public ModelRenderer tooth_lower_9;
    public ModelRenderer lower_front;
    public ModelRenderer lower_back;
    public ModelRenderer lower_left;
    public ModelRenderer lower_right;
    public ModelRenderer head;
    public ModelRenderer tongue_1;
    public ModelRenderer leg_front_left_2;
    public ModelRenderer leg_front_right_2;
    public ModelRenderer leg_back_left_2;
    public ModelRenderer leg_back_right_2;
    public ModelRenderer horn_left_1;
    public ModelRenderer horn_right_1;
    public ModelRenderer head_top;
    public ModelRenderer tooth_upper_1;
    public ModelRenderer tooth_upper_2;
    public ModelRenderer tooth_upper_3;
    public ModelRenderer tooth_upper_4;
    public ModelRenderer tooth_upper_5;
    public ModelRenderer tooth_upper_6;
    public ModelRenderer horn_left_2;
    public ModelRenderer horn_right_2;
    public ModelRenderer tongue_2;
    private float jumpRotation;

    public ModelChester() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.head_top = new ModelRenderer(this, 0, 21);
        this.head_top.setRotationPoint(0.0F, -2.5F, -4.0F);
        this.head_top.addBox(-3.5F, 0.0F, -3.5F, 7, 1, 7, 0.0F);
        this.tooth_upper_1 = new ModelRenderer(this, 14, 31);
        this.tooth_upper_1.setRotationPoint(1.1F, 0.4F, -7.0F);
        this.tooth_upper_1.addBox(-1.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.setRotateAngle(tooth_upper_1, 0.0F, -0.24434609527920614F, 0.0F);
        this.lower_left = new ModelRenderer(this, 40, 30);
        this.lower_left.setRotationPoint(4.0F, -9.0F, 0.0F);
        this.lower_left.addBox(-1.0F, 0.0F, -3.0F, 2, 5, 6, 0.0F);
        this.horn_right_1 = new ModelRenderer(this, 0, 36);
        this.horn_right_1.setRotationPoint(-2.0F, -2.5F, -4.0F);
        this.horn_right_1.addBox(-1.0F, -0.5F, -1.0F, 2, 1, 2, 0.0F);
        this.setRotateAngle(horn_right_1, 0.0F, 0.5235987755982988F, 0.0F);
        this.lower_front = new ModelRenderer(this, 32, 16);
        this.lower_front.setRotationPoint(0.0F, -9.0F, -4.0F);
        this.lower_front.addBox(-5.0F, 0.0F, -1.0F, 10, 5, 2, 0.0F);
        this.horn_left_2 = new ModelRenderer(this, 0, 39);
        this.horn_left_2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.horn_left_2.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(horn_left_2, -0.3490658503988659F, 0.5235987755982988F, -0.3490658503988659F);
        this.tooth_lower_9 = new ModelRenderer(this, 14, 29);
        this.tooth_lower_9.setRotationPoint(4.1F, -10.0F, 1.3F);
        this.tooth_lower_9.addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1, 0.0F);
        this.setRotateAngle(tooth_lower_9, 0.0F, 1.7453292519943295F, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 22.9F, 0.0F);
        this.body.addBox(-5.0F, -4.0F, -5.0F, 10, 1, 10, 0.0F);
        this.leg_back_right_1 = new ModelRenderer(this, 40, 0);
        this.leg_back_right_1.setRotationPoint(-4.0F, -5.0F, 4.0F);
        this.leg_back_right_1.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(leg_back_right_1, -0.5235987755982988F, 2.6179938779914944F, 0.0F);
        this.tooth_lower_5 = new ModelRenderer(this, 14, 31);
        this.tooth_lower_5.setRotationPoint(-4.3F, -10.0F, -1.6F);
        this.tooth_lower_5.addBox(-1.0F, 0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.setRotateAngle(tooth_lower_5, 0.0F, 1.3962634015954636F, 0.0F);
        this.tooth_upper_2 = new ModelRenderer(this, 14, 31);
        this.tooth_upper_2.setRotationPoint(-1.1F, 0.3F, -7.0F);
        this.tooth_upper_2.addBox(-1.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.setRotateAngle(tooth_upper_2, 0.0F, 0.136659280431156F, 0.0F);
        this.tooth_lower_8 = new ModelRenderer(this, 14, 31);
        this.tooth_lower_8.setRotationPoint(4.3F, -9.8F, -1.0F);
        this.tooth_lower_8.addBox(-1.0F, 0.0F, -0.5F, 2, 1, 1, 0.0F);
        this.setRotateAngle(tooth_lower_8, 0.0F, -1.48352986419518F, 0.0F);
        this.lower_back = new ModelRenderer(this, 32, 23);
        this.lower_back.setRotationPoint(0.0F, -9.0F, 4.0F);
        this.lower_back.addBox(-5.0F, 0.0F, -1.0F, 10, 5, 2, 0.0F);
        this.leg_front_left_1 = new ModelRenderer(this, 40, 0);
        this.leg_front_left_1.setRotationPoint(4.0F, -5.0F, -4.0F);
        this.leg_front_left_1.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(leg_front_left_1, -0.5235987755982988F, -0.5235987755982988F, 0.0F);
        this.tongue_2 = new ModelRenderer(this, 0, 47);
        this.tongue_2.setRotationPoint(0.0F, 0.3F, -4.4F);
        this.tongue_2.addBox(-2.0F, -0.5F, -5.5F, 4, 1, 6, 0.0F);
        this.setRotateAngle(tongue_2, 0.296705972839036F, 0.0F, 0.0F);
        this.leg_back_right_2 = new ModelRenderer(this, 40, 8);
        this.leg_back_right_2.setRotationPoint(0.01F, 2.7F, -0.45F);
        this.leg_back_right_2.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(leg_back_right_2, 0.5235987755982988F, 0.0F, 0.0F);
        this.horn_left_1 = new ModelRenderer(this, 0, 32);
        this.horn_left_1.setRotationPoint(2.0F, -2.9F, -4.0F);
        this.horn_left_1.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        this.setRotateAngle(horn_left_1, 0.0F, -0.5235987755982988F, 0.0F);
        this.leg_back_left_2 = new ModelRenderer(this, 40, 8);
        this.leg_back_left_2.setRotationPoint(-0.01F, 2.7F, -0.45F);
        this.leg_back_left_2.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(leg_back_left_2, 0.5235987755982988F, 0.0F, 0.0F);
        this.tooth_lower_4 = new ModelRenderer(this, 14, 29);
        this.tooth_lower_4.setRotationPoint(-3.5F, -10.0F, -3.9F);
        this.tooth_lower_4.addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1, 0.0F);
        this.setRotateAngle(tooth_lower_4, 0.0F, 1.2217304763960306F, 0.0F);
        this.tooth_lower_2 = new ModelRenderer(this, 14, 31);
        this.tooth_lower_2.setRotationPoint(3.7F, -10.0F, -3.7F);
        this.tooth_lower_2.addBox(-1.0F, 0.2F, -0.5F, 2, 1, 1, 0.0F);
        this.setRotateAngle(tooth_lower_2, 0.0F, -0.8726646259971648F, 0.0F);
        this.leg_front_right_1 = new ModelRenderer(this, 40, 0);
        this.leg_front_right_1.setRotationPoint(-4.0F, -5.0F, -4.0F);
        this.leg_front_right_1.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(leg_front_right_1, -0.5235987755982988F, 0.5235987755982988F, 0.0F);
        this.tooth_lower_6 = new ModelRenderer(this, 14, 31);
        this.tooth_lower_6.setRotationPoint(-4.2F, -10.0F, 1.0F);
        this.tooth_lower_6.addBox(-1.0F, 0.3F, -0.5F, 2, 1, 1, 0.0F);
        this.setRotateAngle(tooth_lower_6, 0.0F, 1.7453292519943295F, 0.0F);
        this.tongue_1 = new ModelRenderer(this, 0, 42);
        this.tongue_1.setRotationPoint(0.0F, -9.5F, 1.4F);
        this.tongue_1.addBox(-2.5F, -0.5F, -4.0F, 5, 1, 4, 0.0F);
        this.setRotateAngle(tongue_1, -0.17453292519943295F, 0.0F, 0.0F);
        this.leg_front_right_2 = new ModelRenderer(this, 40, 8);
        this.leg_front_right_2.setRotationPoint(-0.01F, 2.7F, -0.45F);
        this.leg_front_right_2.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(leg_front_right_2, 0.5235987755982988F, 0.0F, 0.0F);
        this.tooth_upper_3 = new ModelRenderer(this, 14, 31);
        this.tooth_upper_3.setRotationPoint(-2.8F, 0.0F, -5.7F);
        this.tooth_upper_3.addBox(-1.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.setRotateAngle(tooth_upper_3, 0.0F, 1.1344640137963142F, 0.0F);
        this.mouth_back = new ModelRenderer(this, 0, 29);
        this.mouth_back.setRotationPoint(0.2F, -9.5F, 4.0F);
        this.mouth_back.addBox(-3.0F, -1.0F, -0.5F, 6, 2, 1, 0.0F);
        this.head = new ModelRenderer(this, 0, 11);
        this.head.setRotationPoint(0.0F, -10.0F, 4.0F);
        this.head.addBox(-4.0F, -2.0F, -8.0F, 8, 2, 8, 0.0F);
        this.setRotateAngle(head, -0.20943951023931953F, 0.0F, 0.0F);
        this.tooth_upper_5 = new ModelRenderer(this, 14, 29);
        this.tooth_upper_5.setRotationPoint(3.0F, 0.5F, -5.5F);
        this.tooth_upper_5.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        this.setRotateAngle(tooth_upper_5, 0.0F, -0.5235987755982988F, 0.0F);
        this.leg_back_left_1 = new ModelRenderer(this, 40, 0);
        this.leg_back_left_1.setRotationPoint(4.0F, -5.0F, 4.0F);
        this.leg_back_left_1.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(leg_back_left_1, -0.5235987755982988F, -2.6179938779914944F, 0.0F);
        this.lower_right = new ModelRenderer(this, 24, 30);
        this.lower_right.setRotationPoint(-4.0F, -9.0F, 0.0F);
        this.lower_right.addBox(-1.0F, 0.0F, -3.0F, 2, 5, 6, 0.0F);
        this.tooth_upper_4 = new ModelRenderer(this, 14, 29);
        this.tooth_upper_4.setRotationPoint(-3.0F, 0.3F, -3.5F);
        this.tooth_upper_4.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        this.setRotateAngle(tooth_upper_4, 0.0F, 1.6580627893946132F, 0.0F);
        this.horn_right_2 = new ModelRenderer(this, 0, 39);
        this.horn_right_2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.horn_right_2.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(horn_right_2, -0.5235987755982988F, 0.5235987755982988F, 0.0F);
        this.tooth_upper_6 = new ModelRenderer(this, 14, 29);
        this.tooth_upper_6.setRotationPoint(2.9F, 0.5F, -3.6F);
        this.tooth_upper_6.addBox(-0.5F, -0.5F, -0.9F, 1, 1, 1, 0.0F);
        this.setRotateAngle(tooth_upper_6, 0.0F, -1.8325957145940461F, 0.0F);
        this.leg_front_left_2 = new ModelRenderer(this, 40, 8);
        this.leg_front_left_2.setRotationPoint(0.01F, 2.7F, -0.45F);
        this.leg_front_left_2.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(leg_front_left_2, 0.5235987755982988F, 0.0F, 0.0F);
        this.head.addChild(this.head_top);
        this.head.addChild(this.tooth_upper_1);
        this.body.addChild(this.lower_left);
        this.head.addChild(this.horn_right_1);
        this.body.addChild(this.lower_front);
        this.horn_left_1.addChild(this.horn_left_2);
        this.body.addChild(this.tooth_lower_9);
        this.body.addChild(this.leg_back_right_1);
        this.body.addChild(this.tooth_lower_5);
        this.head.addChild(this.tooth_upper_2);
        this.body.addChild(this.tooth_lower_8);
        this.body.addChild(this.lower_back);
        this.body.addChild(this.leg_front_left_1);
        this.tongue_1.addChild(this.tongue_2);
        this.leg_back_right_1.addChild(this.leg_back_right_2);
        this.head.addChild(this.horn_left_1);
        this.leg_back_left_1.addChild(this.leg_back_left_2);
        this.body.addChild(this.tooth_lower_4);
        this.body.addChild(this.tooth_lower_2);
        this.body.addChild(this.leg_front_right_1);
        this.body.addChild(this.tooth_lower_6);
        this.body.addChild(this.tongue_1);
        this.leg_front_right_1.addChild(this.leg_front_right_2);
        this.head.addChild(this.tooth_upper_3);
        this.body.addChild(this.mouth_back);
        this.body.addChild(this.head);
        this.head.addChild(this.tooth_upper_5);
        this.body.addChild(this.leg_back_left_1);
        this.body.addChild(this.lower_right);
        this.head.addChild(this.tooth_upper_4);
        this.horn_right_1.addChild(this.horn_right_2);
        this.head.addChild(this.tooth_upper_6);
        this.leg_front_left_1.addChild(this.leg_front_left_2);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        this.body.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
    	
        float f = ageInTicks - (float)entityIn.ticksExisted;
        EntityChester entity = (EntityChester)entityIn;
//        System.out.println(f);
        this.head.rotateAngleX = (entity.lidAngle * -78.0F - 12.0F) * 0.017453292F;
        
        this.jumpRotation = MathHelper.sin(entity.setJumpCompletion(f) * (float)Math.PI);
        
        this.head.rotateAngleX = (this.jumpRotation * -6.0F - 12.0F) * 0.017453292F;
        this.tongue_1.rotateAngleX = (this.jumpRotation * -10.0F - 10.0F) * 0.017453292F;
        
        this.leg_front_left_1.rotateAngleX = (this.jumpRotation * 45.0F - 60.0F) * 0.017453292F;
        this.leg_front_right_1.rotateAngleX = (this.jumpRotation * 45.0F - 60.0F) * 0.017453292F;
        this.leg_front_left_2.rotateAngleX = (this.jumpRotation * -30.0F + 60.0F) * 0.017453292F;
        this.leg_front_right_2.rotateAngleX = (this.jumpRotation * -30.0F + 60.0F) * 0.017453292F;
        this.leg_back_left_1.rotateAngleX = (this.jumpRotation * 45.0F - 60.0F) * 0.017453292F;
        this.leg_back_right_1.rotateAngleX = (this.jumpRotation * 45.0F - 60.0F) * 0.017453292F;
        this.leg_back_left_2.rotateAngleX = (this.jumpRotation * -30.0F + 60.0F) * 0.017453292F;
        this.leg_back_right_2.rotateAngleX = (this.jumpRotation * -30.0F + 60.0F) * 0.017453292F;
    }
    
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime)
    {
        super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
//        this.jumpRotation = MathHelper.sin(((EntityChester)entitylivingbaseIn).setJumpCompletion(partialTickTime) * (float)Math.PI);
    }
}