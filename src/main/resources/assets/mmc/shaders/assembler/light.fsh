varying vec2 texCoord;
uniform vec3 color;
uniform sampler2D bgl_RenderedTexture;

void main(){
    vec4 texel = texture2D(bgl_RenderedTexture, texCoord);

    if(texel.r >= 0.9 && texel.g > 0.9 && texel.b >= 0.9){
        vec4 sum = vec4(0);

        int j;
        int i;

        for(i = -4; i < 4; i++){
            for(j = -3; j < 3; j++){
                sum += texture2D(bgl_RenderedTexture, texCoord + vec2(j, i) * 0.004) * 0.25;
            }
        }

        if(texture2D(bgl_RenderedTexture, texCoord).r < 0.3){
            gl_FragColor = sum * sum * 0.012 + texture2D(bgl_RenderedTexture, texCoord);
        } else{
            if(texture2D(bgl_RenderedTexture, texCoord).r < 0.5){
                gl_FragColor = sum * sum * 0.009 + texture2D(bgl_RenderedTexture, texCoord);
            } else{
                gl_FragColor = sum * sum * 0.0075 + texture2D(bgl_RenderedTexture, texCoord);
            }
        }

        gl_FragColor = gl_FragColor * vec4(color, 1.0);
    } else{
        gl_FragColor = texel;
    }
}