/**
 * Copyright 2011 Morgan Humes
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.milkbowl.vault.modules.economy;

public class EconomyResponse {

    public static enum ResponseType {
        SUCCESS(1),
        FAILURE(2),
        NOT_IMPLEMENTED(3);
        
        private int id;
        ResponseType(int id) {
            this.id = id;
        }
        
        int getId() {
            return id;
        }
    }
    
    public final double amount;
    public final double balance;
    public final ResponseType type;
    public final String errorMessage;
    
    public EconomyResponse(double amount, double balance, ResponseType type, String errorMessage) {
        this.amount = amount;
        this.balance = balance;
        this.type = type;
        this.errorMessage = errorMessage;
    }
    
    public boolean transactionSuccess() {
        switch (type) {
        case SUCCESS:
            return true;
        default:
            return false;
        }
    }
}