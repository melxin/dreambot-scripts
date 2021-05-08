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
package castlewars_sabotager;

import org.dreambot.api.methods.map.Area;

    /**
     * CastleWars locations enumeration
     */
    public enum Locations
    {
        castlewarsLobby(new Area(2435, 3098, 2446, 3080, 0)),
        
        saradominWaitingRoom(new Area(2391, 9483, 2370, 9496, 0)),
        saradominRespawnRoom(new Area(2423, 3080, 2431, 3072, 1)),
        saradominFirstFloor(new Area(2420, 3072, 2422, 3083, 1)),
        saradominGroundFloor(new Area(2431, 3072, 2417, 3086, 0)),
        
        saradominSupply(new Area(2424, 3077, 2431, 3072, 0)),
        
        barricadeSetupArea(new Area(2413, 3072, 2377, 3096, 0));
        //barricadeSetupArea(new Area(2400, 3072, 2377, 3092, 0));
        
        /*zamorakWaitingRoom(new Area(2429, 9515, 2412, 9518, 0)),
        zamorakRespawnRoom(new Area(2368, 3135, 2376, 3127, 1)),
        zamorakFirstFloor(new Area(2377, 3135, 2379, 3124, 1)),
        zamorakMainFloor(new Area(2368, 3135, 2382, 3121, 0));*/

        /**
         * Area variable
         */
        private final Area area;

        /**
         * Locations setter
         * 
         * @param area
         */
        private Locations(Area area) 
        {
            this.area = area;
        }

        /**
         * Area getter
         * 
         * @return area
         */
        public Area getArea() 
        {
            return area;
        }
}
