import { Injectable } from '@angular/core';
import {RequestFactoryServiceBase} from '../../../../shared/services/request-factory-service.abstract';

@Injectable({
  providedIn: 'root'
})
export class ResGalaxyRequestFactoryService extends RequestFactoryServiceBase {

  constructor() {
    super();
  }
}
