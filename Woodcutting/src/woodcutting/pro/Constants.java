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
package woodcutting.pro;

public class Constants 
{
    // Variables
    private static final String Name = "Woodcutter Pro";
    private static String Status;
    private static String Tree;
    private static boolean Start;

    private static int stopLvl;

    // Getters & Setters
    protected static int setStop(int lvl) 
    {
        return stopLvl = lvl;
    }

    protected static int getStop() 
    {
        return stopLvl;
    }

    protected static String getName() 
    {
        return Name;
    }

    protected static String setStatus(String status) 
    {
        return Status = status;
    }

    protected static String getStatus() 
    {
        return Status;
    }

    protected static String setTree(String tree) 
    {
        return Tree = tree;

    }

    protected static String getTree() 
    {
        return Tree;
    }

    protected static boolean setStart(boolean value) 
    {
        return Start = value;

    }

    protected static boolean getStart() 
    {
        return Start;
    }

}
