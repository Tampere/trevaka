// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import type { Config } from "@jest/types";
import { createConfig } from "./jest.config";

const config: Config.InitialOptions = {
  ...createConfig("vesilahti"),
  testMatch: ["<rootDir>/specs/**/*translations.spec.ts"],
};

export default config;
