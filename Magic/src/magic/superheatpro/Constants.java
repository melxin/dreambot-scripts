/*
 * Copyright (c) 2021, 7ctx <https://github.com/7ctx/> 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package magic.superheatpro;

public class Constants 
{
    // Variables
    private static final int nature_rune = 561;
    private static int _ore;
    private static int _bar;

    //int iron_ore = 440;
    //int iron_bar = 2351;
    //int gold_ore = 444;
    //int gold_bar = 2357;
    
    /**
     * Nature rune Getter
     *
     * @return nature_rune
     */
    protected static int getNatureRune() 
    {
        return nature_rune;
    }

    /**
     * Ore Setter
     *
     * @param ore
     */
    protected static void setOre(int ore) 
    {
        _ore = ore;
    }

    /**
     * Ore Getter
     *
     * @return _ore
     */
    protected static int getOre() 
    {
        return _ore;
    }

    /**
     * Bar Setter
     *
     * @param bar
     */
    protected static void setBar(int bar) 
    {
        _bar = bar;
    }

    /**
     * Bar Getter
     *
     * @return _bar
     */
    protected static int getBar() 
    {
        return _bar;
    }

}
