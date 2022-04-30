import { Injectable } from '@angular/core';
import {Period} from '../../models/period/period.model';

@Injectable({
  providedIn: 'root'
})
export class PeriodService {

  constructor() { }

  // Creating period by start and stop values
  static createPeriod(dateStart: Date, dateStop: Date): Period {
    const period = new Period();

    dateStart.setUTCHours(0, 0, 0, 0);
    dateStop.setUTCHours(23, 59, 59, 999);

    period.start = dateStart.toISOString();
    period.stop = dateStop.toISOString();

    return period;
  }

  // Creating period for current week (from Monday to Sunday)
  static getCurrentWeek(): Period {
    const date = new Date();

    // Correcting first day of week to Monday instead of Sunday
    const dateStart = new Date(
      date.getTime() - ((new Date().getDay() - 1) * 24 * 60 * 60 * 1000) - date.getTimezoneOffset() * 60 * 1000
    );
    const dateStop = new Date(
      date.getTime() + ((6 - (new Date().getDay() - 1)) * 24 * 60 * 60 * 1000) - date.getTimezoneOffset() * 60 * 1000
    );

    return this.createPeriod(dateStart, dateStop);
  }

  // Creating period for current month (from 1 to last day of month)
  static getCurrentMonth(): Period {
    const date = new Date();
    const dateStart = new Date(date.getFullYear(), date.getMonth(), 2);               // 2 -> 2 day of month - 2 hours = 1 day of month 22:00
    const dateStop = new Date(date.getFullYear(), date.getMonth() + 1, 1);     // getMonth + 1 -> next month, 1 -> 1 day of month - 2 hours = last day of previous month 22:00

    return this.createPeriod(dateStart, dateStop);
  }

  // Creating period for current year (from 1 to last day of year)
  static getCurrentYear(): Period {
    const date = new Date();
    const dateStart = new Date(date.getFullYear(), 0, 2);
    const dateStop = new Date(date.getFullYear() + 1, 0, 1);

    return this.createPeriod(dateStart, dateStop);
  }
}
