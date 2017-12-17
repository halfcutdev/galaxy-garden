package com.halfcut.galaxygarden.ui.hud;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.halfcut.galaxygarden.level.object.entity.Player;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.util.Timer;
import com.halfcut.galaxygarden.level.object.weapon.player.WeaponRifle;
import com.halfcut.galaxygarden.level.object.weapon.player.WeaponShotgun;

/**
 * @author halfcutdev
 * @since 10/09/2017
 */
public class HUD {

    private Player player;

    // health
    private TextureRegion heartFull;
    private TextureRegion heartHalf;
    private TextureRegion heartEmpty;
    private boolean showHealth;
    private Timer healthDisplayTimer;

    // rifle
    private TextureRegion bullet1;
    private TextureRegion bullet1Empty;
    private TextureRegion bullet2;
    private TextureRegion bullet2Empty;

    // shotgun
    private TextureRegion shell;
    private TextureRegion shellEmpty;

    public HUD(Player player) {

        this.player = player;

        // health
        heartFull  = Assets.get().getAtlas().findRegion("hud/heart_full");
        heartHalf  = Assets.get().getAtlas().findRegion("hud/heart_half");
        heartEmpty = Assets.get().getAtlas().findRegion("hud/heart_empty");
        showHealth = true;
        healthDisplayTimer = new Timer(3000, true);

        // rifle
        bullet1      = Assets.get().getAtlas().findRegion("hud/bullet1");
        bullet1Empty = Assets.get().getAtlas().findRegion("hud/bullet1_empty");
        bullet2      = Assets.get().getAtlas().findRegion("hud/bullet2");
        bullet2Empty = Assets.get().getAtlas().findRegion("hud/bullet2_empty");

        // shotgun
        shell      = Assets.get().getAtlas().findRegion("hud/shotgun_shell");
        shellEmpty = Assets.get().getAtlas().findRegion("hud/shotgun_shell_empty");
    }

    public void update(float delta) {

        if(healthDisplayTimer.tick(delta)) {
            healthDisplayTimer.stop();
            healthDisplayTimer.reset();
            showHealth = false;
        }
    }

    public void draw(SpriteBatch sb, ShapeRenderer sr) {

        if(showHealth) {
            int spacing = 2;
            float x = player.pos.x + (player.width - (heartFull.getRegionWidth() + spacing) * player.getHearts()) / 2 + spacing;
            float y = player.pos.y + player.height + heartFull.getRegionHeight();

            for(int i=0; i<player.getHearts(); i++) {
                int health = i*2 + 2;
                float x1 = x + i * (heartFull.getRegionWidth() + spacing);
                if(player.getHealth() >= health)   sb.draw(heartFull, x1, y);
                if(player.getHealth() == health-1) sb.draw(heartHalf, x1, y);
                if(player.getHealth() <  health-1) sb.draw(heartEmpty, x1, y);
            }
        }

        if(player.getWeapon() instanceof WeaponRifle) {

            WeaponRifle weapon = (WeaponRifle) player.getWeapon();
            float x = player.pos.x + (player.width - weapon.STARTING_AMMO * bullet1.getRegionWidth()) / 2 + 2;
            float y = player.pos.y + player.height - 18;


                for(int i=0; i<weapon.STARTING_AMMO; i++) {
                    boolean light = (i % 2 == 0);
                    TextureRegion tex;
                    if(i >= weapon.getAmmo()) {
                        tex = (light) ? bullet1Empty : bullet2Empty;
                    } else {
                        tex = (light) ? bullet1 : bullet2;
                    }
                    sb.draw(tex, x + i*bullet1.getRegionWidth(), y);
                }

        }

        if(player.getWeapon() instanceof WeaponShotgun) {
            WeaponShotgun weapon = (WeaponShotgun) player.getWeapon();
            float x = player.pos.x + (player.width - weapon.STARTING_AMMO * (shell.getRegionWidth() + 1)) / 2 + 2;
            float y = player.pos.y + player.height - 18;

            for(int i=0; i<weapon.STARTING_AMMO; i++) {
                TextureRegion tex = (i >= weapon.getAmmo()) ? shellEmpty : shell;
                sb.draw(tex, x + i*(shell.getRegionWidth()+1), y);
            }

        }
    }

    public void showHealth() {

        showHealth = true;
        healthDisplayTimer.reset();
        healthDisplayTimer.start();
    }

}
